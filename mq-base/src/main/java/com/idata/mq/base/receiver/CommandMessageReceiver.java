package com.idata.mq.base.receiver;

import com.idata.mq.base.message.CommandMessage;

public interface CommandMessageReceiver {
    void receive(CommandMessage commandMessage);
}
