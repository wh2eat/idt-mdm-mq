package com.idata.mq.base.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.idata.mq.base.message.ServerStatusMessage;
import com.idata.mq.base.receiver.ServerStatusMessageReceiver;

public class ServiceStatusMessageListener extends BaseMessageListener<ServerStatusMessage> {

    private final static Logger LOGGER = LogManager.getLogger(ServiceStatusMessageListener.class);

    public ServiceStatusMessageListener() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private ServerStatusMessageReceiver messageReceiver;

    @Override
    public void onMessage(ServerStatusMessage message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][onMessage][" + message.getClass().getSimpleName() + "][" + message.getMessageId() + "]");
        }
        if (null != messageReceiver) {
            messageReceiver.receive(message);
        }
        else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][onMessage][messageReceiver is null][" + message.getClass().getSimpleName() + "]["
                        + message.getMessageId() + "]");
            }
        }
    }

}
