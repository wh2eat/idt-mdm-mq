package com.idata.mq.base.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import com.idata.mq.base.constant.FailMessageCodeConstants;
import com.idata.mq.base.event.MessageAckFailedEvent;
import com.idata.mq.base.message.FailMessage;
import com.idata.mq.base.receiver.FailMessageReceiver;

public class FailMessageListener extends BaseMessageListener<FailMessage> {

    private final static Logger LOGGER = LogManager.getLogger(FailMessageListener.class);

    public FailMessageListener() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private FailMessageReceiver messageReceiver;

    @Override
    public void onMessage(FailMessage message) {
        messageReceiver.receive(message);
    }

    @EventListener
    public void process(MessageAckFailedEvent sendFailedMessageEvent) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][process][SendFailedMessageEvent," + sendFailedMessageEvent.getMessage() + "]");
        }

        FailMessage failMessage = new FailMessage();
        failMessage.setCode(FailMessageCodeConstants.SEND_ACK_FAILED);
        failMessage.setMessage(sendFailedMessageEvent.getMessage());

        sendFailedMessageEvent = null;

        onMessage(failMessage);
    }
}
