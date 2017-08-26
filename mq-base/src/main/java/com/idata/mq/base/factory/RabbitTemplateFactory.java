package com.idata.mq.base.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.support.ValueExpression;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitTemplateFactory {

    private final static Logger LOGGER = LogManager.getLogger(RabbitTemplateFactory.class);

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private ConfirmCallback confirmCallback;

    @Autowired
    private ReturnCallback returnCallback;

    @Autowired
    private RetryTemplate retryTemplate;

    @Autowired
    private MessageConverter jsonMessageConverter;

    public RabbitTemplate createRabbitTemplate(String exchange) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(exchange);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setRetryTemplate(retryTemplate);

        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setMandatoryExpression(new ValueExpression<Boolean>(true));

        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][createRabbitTemplate][exchange:" + exchange + "]");
        }

        return rabbitTemplate;
    }
}
