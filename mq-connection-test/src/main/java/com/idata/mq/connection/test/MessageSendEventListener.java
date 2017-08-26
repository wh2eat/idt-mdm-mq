package com.idata.mq.connection.test;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import com.idata.mq.base.exception.SendMessageException;
import com.idata.mq.base.message.DeviceMessage;
import com.idata.mq.connection.service.Connection2MdmMessageSendService;

public class MessageSendEventListener {

    @Autowired
    private Connection2MdmMessageSendService messageSenderService;

    @EventListener
    @Async
    public void process(DeviceMessage deviceMessage) {
        try {
            messageSenderService.sendMessageResult(deviceMessage.getMessageId(), RandomUtils.nextInt(0, 2));
        }
        catch (SendMessageException e) {
            e.printStackTrace();
        }
    }

}
