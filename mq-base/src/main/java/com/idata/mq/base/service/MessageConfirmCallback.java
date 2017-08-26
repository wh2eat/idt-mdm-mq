package com.idata.mq.base.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import com.idata.mq.base.event.ConfirmCallbackEvent;

@Component
public class MessageConfirmCallback implements ConfirmCallback, ApplicationEventPublisherAware {

    private final static Logger LOGGER = LogManager.getLogger(MessageConfirmCallback.class);

    private MessageSendService messageSendService;

    public MessageConfirmCallback() {
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][confirm][correlationData:" + correlationData + ";ack:" + ack + ";case:" + cause);
        }

        if (null != correlationData) {
            String id = correlationData.getId();
            if (StringUtils.isNotBlank(id)) {
                ConfirmCallbackEvent event = new ConfirmCallbackEvent(id, ack);
                eventPublisher.publishEvent(event);
            }
        }
    }

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        eventPublisher = applicationEventPublisher;

    }

    public MessageSendService getMessageSendService() {
        return messageSendService;
    }

    public void setMessageSendService(MessageSendService messageSendService) {
        this.messageSendService = messageSendService;
    }
}
