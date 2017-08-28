package com.idata.mq.base.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.idata.mq.base.message.ServerStatusMessage;
import com.idata.mq.base.receiver.ServerStatusMessageReceiver;

public class ServiceStatusMessageListener extends BaseMessageListener<ServerStatusMessage> {

    private final static Logger LOGGER = LogManager.getLogger(ServiceStatusMessageListener.class);

    public ServiceStatusMessageListener() {
    }

    @Autowired
    private ServerStatusMessageReceiver messageReceiver;

    @Override
    public void onMessage(ServerStatusMessage message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][onMessage][ServerStatusMessage][" + message + "]");
        }
        if (null != messageReceiver) {
            messageReceiver.receive(message);
        }
        else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][onMessage][ServerStatusMessageReceiver is null]");
            }
        }
    }

}
