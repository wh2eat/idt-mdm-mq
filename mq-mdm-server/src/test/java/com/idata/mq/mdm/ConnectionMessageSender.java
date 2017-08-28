package com.idata.mq.mdm;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.idata.mq.base.message.BaseMessage;
import com.idata.mq.base.util.RandomGeneratorUtil;

public class ConnectionMessageSender {

    @Autowired
    private RabbitTemplate connDirectTemplate;

    private TimeBasedGenerator generator = RandomGeneratorUtil.getTimeBasedGenerator();

    public void sendMessage(String routingKey, BaseMessage message) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(generator.generate().toString());
        connDirectTemplate.convertAndSend(routingKey, message, correlationData);
    }
}
