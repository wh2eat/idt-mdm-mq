package com.idata.mq.base.receiver;

import com.idata.mq.base.message.DeviceMessage;

public interface DeviceMessageReceiver {

    void receive(DeviceMessage deviceMessage);

}
