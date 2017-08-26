package com.idata.mq.base.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;

import com.idata.mq.base.constant.FailMessageCodeConstants;
import com.idata.mq.base.event.BackProcessFailedMessageEvent;
import com.idata.mq.base.event.ConfirmCallbackEvent;
import com.idata.mq.base.event.MessageAckFailedEvent;
import com.idata.mq.base.exception.SendMessageException;
import com.idata.mq.base.message.BaseMessage;
import com.idata.mq.base.message.FailMessage;
import com.idata.mq.base.message.ServerStatusMessage;
import com.idata.mq.base.properties.AmqProperties;
import com.idata.mq.base.util.RandomGeneratorUtil;

public abstract class AbstractMessageSendService implements MessageSendService, ApplicationEventPublisherAware {

    private final static Logger LOGGER = LogManager.getLogger(AbstractMessageSendService.class);

    private Map<String, BaseMessage> message_cache = new ConcurrentHashMap<>();

    protected String targetServerName;

    private volatile long targetHeartbeatFlushMillis = System.currentTimeMillis();

    private volatile boolean targetServerRunning = true;

    private AmqProperties amqProperties;

    private Thread monitorThread = new Thread(new Runnable() {

        @Override
        public void run() {
            LOGGER.info("[][][monitorThread run][" + amqProperties + "]");

            while (true) {
                if (LOGGER.isDebugEnabled()) {
                    int size = message_cache.size();
                    if (size > 0) {
                        LOGGER.debug("[][][monitor message cache size:" + size + "]");
                        System.out.println("[][][monitor message cache size:" + size + "]");
                    }
                }

                if (amqProperties.isCheckHeartbeat()) {
                    try {
                        sendAlive();
                    }
                    catch (SendMessageException e1) {
                        LOGGER.error("[][][sendAlive failed]", e1);
                    }
                    long nowMillis = System.currentTimeMillis();
                    long diff = nowMillis - targetHeartbeatFlushMillis;
                    if (diff <= 120 * 1000) {
                        if (!targetServerRunning) {
                            targetServerRunning = true;
                        }
                    }
                    else {
                        if (targetServerRunning) {
                            targetServerRunning = false;
                            LOGGER.error("[][][" + targetServerName + " is not running]");
                        }
                    }
                }
                try {
                    Thread.currentThread();
                    Thread.sleep(60 * 1000);
                }
                catch (InterruptedException e) {
                    LOGGER.error("[][InterruptedException][]", e);
                }
            }
        }
    }, "message-send-status-monitor-thread");

    protected void init() {
        monitorThread.start();
    }

    public AbstractMessageSendService(AmqProperties amqProperties, String targetServerName) {
        this.amqProperties = amqProperties;
        this.targetServerName = targetServerName;
    }

    @EventListener
    public final void processEvent(ServerStatusMessage statusMessage) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][receiveServerStatusMessage][" + statusMessage + "]");
        }
        String serverName = statusMessage.getServerName();
        if (serverName.equals(targetServerName)) {
            targetHeartbeatFlushMillis = System.currentTimeMillis();
        }
    }

    @EventListener
    public void processEvent(BackProcessFailedMessageEvent backEvent) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][processEvent][BackProcessFailedMessageEvent][" + backEvent.toString() + "]");
        }

        String data = backEvent.getData();
        String dataClassName = backEvent.getDataClassName();

        backEvent = null;

        FailMessage failMessage = new FailMessage();
        failMessage.setCode(FailMessageCodeConstants.CONSUMER_EXCEPTION);
        failMessage.setClassName(dataClassName);
        failMessage.setData(data);
        try {
            sendFailMessage(failMessage);
        }
        catch (SendMessageException e1) {
            LOGGER.error("[][][fail message send execption]", e1);
        }
    }

    @EventListener
    public void processEvent(ConfirmCallbackEvent callbackEvent) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][processEvent][ConfirmCallbackEvent][" + callbackEvent + "]");
        }
        boolean ack = callbackEvent.isAck();
        String correlationId = callbackEvent.getCorrelationId();
        try {
            if (!ack) {
                BaseMessage message = get(correlationId);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.info("[][][ack false][" + message + "]");
                }
                if (!(message instanceof FailMessage)) {
                    MessageAckFailedEvent sendFailedMessageEvent = new MessageAckFailedEvent(message);
                    eventPublisher.publishEvent(sendFailedMessageEvent);
                }
            }
        }
        finally {
            removeMonitor(correlationId);
        }
    }

    protected BaseMessage get(String mesageId) {
        if (message_cache.containsKey(mesageId)) {
            return message_cache.get(mesageId);
        }
        return null;
    }

    protected void removeMonitor(String msgId) {
        if (null != msgId && message_cache.containsKey(msgId)) {
            message_cache.remove(msgId);
        }
        else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][removeMonitor][not found messasge by id:" + msgId + "]");
            }
        }
    }

    protected void addToMonitor(BaseMessage message) {
        message_cache.put(message.getMessageId(), message);
    }

    protected void sendMessage(String routingKey, BaseMessage message) throws SendMessageException {

        if (!isRunningForTargetServer()) {
            throw new SendMessageException(
                    targetServerName + " is not running,case:not accept server Heartbeat in 120 seconds");
        }

        String msgId = message.getMessageId();
        if (StringUtils.isEmpty(msgId)) {
            msgId = RandomGeneratorUtil.getTimeBasedGenerator().generate().toString();
            message.setMessageId(msgId);
        }

        addToMonitor(message);

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(msgId);

        try {
            doSendMessage(routingKey, message, correlationData);
        }
        catch (AmqpException e) {
            removeMonitor(msgId);
            throw new SendMessageException(
                    "[][][doSendMessage error,thrwos AmqpException:" + e.getLocalizedMessage() + "][" + message + "]",
                    e);
        }
    }

    // 执行消息发送
    protected abstract void doSendMessage(String routingKey, BaseMessage message, CorrelationData correlationData)
            throws SendMessageException;

    protected abstract void doResendMessage(BaseMessage message) throws SendMessageException;

    private boolean isRunningForTargetServer() {
        if (!amqProperties.isCheckHeartbeat()) {
            return true;
        }
        return targetServerRunning;
    }

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

}
