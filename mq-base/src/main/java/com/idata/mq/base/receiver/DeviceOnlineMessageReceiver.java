package com.idata.mq.base.receiver;

import com.idata.mq.base.message.DeviceOnlineMessage;

public interface DeviceOnlineMessageReceiver {
    void receive(DeviceOnlineMessage deviceOnlineMessage);
}
