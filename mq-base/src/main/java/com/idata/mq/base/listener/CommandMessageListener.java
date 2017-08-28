package com.idata.mq.base.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.idata.mq.base.message.CommandMessage;
import com.idata.mq.base.receiver.CommandMessageReceiver;

public class CommandMessageListener extends BaseMessageListener<CommandMessage> {

    private final static Logger LOGGER = LogManager.getLogger(CommandMessageListener.class);

    public CommandMessageListener() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private CommandMessageReceiver messageReceiver;

    @Override
    public void onMessage(CommandMessage message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][onMessage][CommandMessage][" + message + "]");
        }
        if (null != messageReceiver) {
            messageReceiver.receive(message);
        }
        else {
            LOGGER.warn("[][onMessage][CommandMessageReceiver is null]");
        }
    }
}
