package com.idata.mq.base.receiver;

import com.idata.mq.base.message.FailMessage;

public interface FailMessageReceiver {
    void receive(FailMessage failMessage);
}
