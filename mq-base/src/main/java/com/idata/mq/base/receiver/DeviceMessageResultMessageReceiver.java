package com.idata.mq.base.receiver;

import com.idata.mq.base.message.DeviceMessageResultMessage;

public interface DeviceMessageResultMessageReceiver {
    void receive(DeviceMessageResultMessage deviceMessageResultMessage);
}
