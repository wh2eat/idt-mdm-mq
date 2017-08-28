package com.idata.mq.base.listener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.alibaba.fastjson.JSONObject;
import com.idata.mq.base.event.BackProcessFailedMessageEvent;
import com.idata.mq.base.message.ServerStatusMessage;
import com.rabbitmq.client.Channel;

public abstract class BaseMessageListener<T> implements ChannelAwareMessageListener, ApplicationEventPublisherAware {

    private static final String UNCHECKED = "unchecked";

    private Logger logger = LogManager.getLogger(BaseMessageListener.class);

    protected Class entityClass;

    public BaseMessageListener() {
        entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Autowired
    private ScheduledThreadPoolExecutor scheduledExecutor;

    @Override
    public void onMessage(final Message message, final Channel channel) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "[][onMessage][CorrelationId:" + message.getMessageProperties().getCorrelationIdString() + "]");
        }

        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
        catch (IOException e) {
            try {
                Thread.currentThread();
                Thread.sleep(RandomUtils.nextLong(10, 100));
            }
            catch (InterruptedException e2) {
            }
            if (logger.isDebugEnabled()) {
                logger.debug("[][][basic ack failed,retry]");
            }
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
            catch (IOException e2) {
                logger.error("[][][retry basic ack failed]", e2);
            }
        }

        scheduledExecutor.execute(new Runnable() {

            @Override
            public void run() {

                String messageBody = null;
                try {
                    messageBody = new String(message.getBody(), "utf-8");
                }
                catch (UnsupportedEncodingException e) {
                    logger.error("[][][]", e);
                }

                if (StringUtils.isNotBlank(messageBody)) {
                    @SuppressWarnings(UNCHECKED)
                    Object messageObj = JSONObject.toJavaObject(JSONObject.parseObject(messageBody), entityClass);
                    try {
                        T t = (T) messageObj;
                        if (t instanceof ServerStatusMessage) {
                            eventPublisher.publishEvent(t);
                        }
                        onMessage(t);
                    }
                    catch (Exception e) {
                        BackProcessFailedMessageEvent backEvent = new BackProcessFailedMessageEvent(
                                entityClass.getName(), messageBody);
                        eventPublisher.publishEvent(backEvent);
                        logger.error("[][][onMessage error]", e);
                    }
                }
                else {
                    logger.warn("[][][message body is empty][" + message + "]");
                }
            }
        });
    }

    public abstract void onMessage(T message);

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        eventPublisher = applicationEventPublisher;
    }

}
