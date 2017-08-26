package com.idata.mq.mdm;

import com.idata.mq.base.receiver.DeviceMessageResultMessageReceiver;
import com.idata.mq.base.receiver.DeviceOfflineMessageReceiver;
import com.idata.mq.base.receiver.DeviceOnlineMessageReceiver;
import com.idata.mq.base.receiver.FailMessageReceiver;
import com.idata.mq.base.receiver.ServerStatusMessageReceiver;

public abstract class AbstractMessageReceiver implements DeviceMessageResultMessageReceiver,
        DeviceOnlineMessageReceiver, DeviceOfflineMessageReceiver, ServerStatusMessageReceiver, FailMessageReceiver {

}
