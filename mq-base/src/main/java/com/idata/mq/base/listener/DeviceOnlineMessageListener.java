package com.idata.mq.base.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.idata.mq.base.message.DeviceOnlineMessage;
import com.idata.mq.base.receiver.DeviceOnlineMessageReceiver;

public class DeviceOnlineMessageListener extends BaseMessageListener<DeviceOnlineMessage> {

    private final static Logger LOGGER = LogManager.getLogger(DeviceOnlineMessageListener.class);

    public DeviceOnlineMessageListener() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private DeviceOnlineMessageReceiver messageReceiver;

    @Override
    public void onMessage(DeviceOnlineMessage message) {
        messageReceiver.receive(message);
    }
}
