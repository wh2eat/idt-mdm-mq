package com.idata.mq.connection;

import com.idata.mq.base.receiver.CommandMessageReceiver;
import com.idata.mq.base.receiver.DeviceMessageReceiver;
import com.idata.mq.base.receiver.FailMessageReceiver;
import com.idata.mq.base.receiver.ServerStatusMessageReceiver;

public abstract class AbstractMessageReceiver
        implements CommandMessageReceiver, ServerStatusMessageReceiver, DeviceMessageReceiver, FailMessageReceiver {

}
