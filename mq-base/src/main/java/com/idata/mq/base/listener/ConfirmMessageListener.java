package com.idata.mq.base.listener;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.alibaba.fastjson.JSONObject;

public class ConfirmMessageListener implements MessageListener {

    private final static Logger LOGGER = LogManager.getLogger(ConfirmMessageListener.class);

    public ConfirmMessageListener() {
    }

    private ScheduledThreadPoolExecutor scheduledExecutor;

    @Override
    public void onMessage(Message message) {

        final String body = new String(message.getBody());

        if (StringUtils.isNotBlank(body)) {
            scheduledExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    JSONObject confirmJsonMessge = JSONObject.parseObject(body);
                    String messageId = confirmJsonMessge.getString("sourceMessageId");
                    // baseMessageSendService.removeMonitor(messageId);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("[ConfirmMessageListener][onMessage][messageId:" + messageId + ",remove monitor]");
                    }
                    confirmJsonMessge = null;
                }
            });
        }

        // String messageId = confirmMessage.getSourceMessageId();
        // baseMessageSendService.removeMonitor(messageId);
        // if (LOGGER.isDebugEnabled()) {
        // LOGGER.debug("[ConfirmMessageListener][onMessage][messageId:" + messageId +
        // ",remove monitor]");
        // }
        // confirmMessage = null;

        // if (LOGGER.isDebugEnabled()) {
        // LOGGER.debug("[ConfirmMessageListener][onMessage][" + body + "]");
        // }
        // ConfirmMessage confirmMessage =
        // JSONObject.toJavaObject(JSONObject.parseObject(body), ConfirmMessage.class);
        // if (null != confirmMessage) {
        // String messageId = confirmMessage.getSourceMessageId();
        // baseMessageSendService.removeMonitor(messageId);
        // if (LOGGER.isDebugEnabled()) {
        // LOGGER.debug("[ConfirmMessageListener][onMessage][messageId:" + messageId +
        // ",remove monitor]");
        // }
        // confirmMessage = null;
        // }
        //
        // body = null;
    }
}
