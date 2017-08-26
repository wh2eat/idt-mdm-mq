package com.idata.mq.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.idata.mq.base.message.FailMessage;
import com.idata.mq.base.receiver.FailMessageReceiver;

@Service
public class FailMessageReceiveService {

    @Autowired
    private FailMessageReceiver failMessageReceiver;

    public FailMessageReceiveService() {

    }

    @EventListener
    public void receive(FailMessage failMessage) {
        failMessageReceiver.receive(failMessage);
    }
}
