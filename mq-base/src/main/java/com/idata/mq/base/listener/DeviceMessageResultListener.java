package com.idata.mq.base.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.idata.mq.base.message.DeviceMessageResultMessage;
import com.idata.mq.base.receiver.DeviceMessageResultMessageReceiver;

public class DeviceMessageResultListener extends BaseMessageListener<DeviceMessageResultMessage> {

    private final static Logger LOGGER = LogManager.getLogger(DeviceMessageResultListener.class);

    public DeviceMessageResultListener() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private DeviceMessageResultMessageReceiver messageReceiver;

    @Override
    public void onMessage(DeviceMessageResultMessage message) {
        messageReceiver.receive(message);
    }

}
