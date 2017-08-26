package com.idata.mq.base.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.stereotype.Component;

@Component
public class MessageReturnCallback implements ReturnCallback {

    private final static Logger LOGGER = LogManager.getLogger(MessageReturnCallback.class);

    public MessageReturnCallback() {
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("[][returnedMessage][message:" + message + ";replyCode:" + replyCode + ";replyText:" + replyText
                    + ";exchange:" + exchange + ";routingKey:" + routingKey + "]");
        }
    }

}
