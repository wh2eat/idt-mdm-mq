package com.idata.mq.base.receiver;

import com.idata.mq.base.message.DeviceOfflineMessage;

public interface DeviceOfflineMessageReceiver {
    void receive(DeviceOfflineMessage deviceOfflineMessage);
}
