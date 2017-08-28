package com.idata.mq.base.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.idata.mq.base.message.DeviceMessage;
import com.idata.mq.base.receiver.DeviceMessageReceiver;

public class DeviceMessageListener extends BaseMessageListener<DeviceMessage> {

    private final static Logger LOGGER = LogManager.getLogger(DeviceMessageListener.class);

    public DeviceMessageListener() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private DeviceMessageReceiver messageReceiver;

    @Override
    public void onMessage(DeviceMessage message) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][onMessage][DeviceMessage][" + message + "]");
        }
        if (null != messageReceiver) {
            messageReceiver.receive(message);
        }
        else {
            LOGGER.warn("[][onMessage][DeviceMessageReceiver is null]");
        }
    }

}
