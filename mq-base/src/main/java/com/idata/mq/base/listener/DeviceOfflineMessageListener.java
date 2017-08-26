package com.idata.mq.base.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.idata.mq.base.message.DeviceOfflineMessage;
import com.idata.mq.base.receiver.DeviceOfflineMessageReceiver;

public class DeviceOfflineMessageListener extends BaseMessageListener<DeviceOfflineMessage> {

    private final static Logger LOGGER = LogManager.getLogger(DeviceOfflineMessageListener.class);

    public DeviceOfflineMessageListener() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private DeviceOfflineMessageReceiver messageReceiver;

    @Override
    public void onMessage(DeviceOfflineMessage message) {
        messageReceiver.receive(message);
    }

}
