package com.idata.mq.base.event;

import com.idata.mq.base.message.BaseMessage;

public class MessageAckFailedEvent {

    private BaseMessage message;

    public MessageAckFailedEvent(BaseMessage message) {
        this.message = message;
    }

    public BaseMessage getMessage() {
        return message;
    }

    public void setMessage(BaseMessage message) {
        this.message = message;
    }

}
