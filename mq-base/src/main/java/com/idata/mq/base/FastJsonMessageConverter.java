package com.idata.mq.base;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import com.alibaba.fastjson.JSON;

public class FastJsonMessageConverter extends AbstractMessageConverter {

    public static final String DEFAULT_CHARSET = "UTF-8";

    private volatile String defaultCharset = DEFAULT_CHARSET;

    private final static Logger LOGGER = LogManager.getLogger(FastJsonMessageConverter.class);

    public FastJsonMessageConverter() {
        super();

    }

    public void setDefaultCharset(String defaultCharset) {

        this.defaultCharset = (defaultCharset != null) ? defaultCharset

                : DEFAULT_CHARSET;

    }

    @Override
    public Object fromMessage(Message message)

            throws MessageConversionException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][fromMessage][" + message + "]");
        }

        return null;

    }

    public <T> T fromMessage(Message message, T t) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][fromMessage to class][" + message + "]");
        }

        String json = "";
        try {
            json = new String(message.getBody(), defaultCharset);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return (T) JSON.parseObject(json, t.getClass());
    }

    @Override
    protected Message createMessage(Object objectToConvert, MessageProperties messageProperties)
            throws MessageConversionException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][createMessage][" + objectToConvert + "][" + messageProperties + "]");
        }

        byte[] bytes = null;
        try {
            String jsonString = JSON.toJSONString(objectToConvert);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][createMessage][jsonString:" + jsonString + "]");
            }
            bytes = jsonString.getBytes(this.defaultCharset);
        }
        catch (UnsupportedEncodingException e) {
            throw new MessageConversionException("Failed to convert Message content", e);
        }
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        messageProperties.setContentEncoding(this.defaultCharset);
        if (bytes != null) {
            messageProperties.setContentLength(bytes.length);
        }
        return new Message(bytes, messageProperties);
    }

}
