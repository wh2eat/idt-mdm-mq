package com.idata.mq.base.receiver;

import com.idata.mq.base.message.ServerStatusMessage;

public interface ServerStatusMessageReceiver {
    void receive(ServerStatusMessage serverStatusMessage);
}
