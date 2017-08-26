package com.idata.mq.connection.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import com.idata.mq.base.message.CommandMessage;
import com.idata.mq.base.message.DeviceMessage;
import com.idata.mq.base.message.FailMessage;
import com.idata.mq.base.message.ServerStatusMessage;
import com.idata.mq.base.util.FailMessageUtil;
import com.idata.mq.connection.AbstractMessageReceiver;

public class MessageReceiver extends AbstractMessageReceiver implements ApplicationEventPublisherAware {

    private final static Logger Logger = LogManager.getLogger(MessageReceiver.class);

    public MessageReceiver() {
    }

    @Override
    public void receive(FailMessage failMessage) {
        Logger.info("[][revice][FailMessage:" + FailMessageUtil.getMessage(failMessage) + "]");
    }

    @Override
    public void receive(DeviceMessage deviceMessage) {
        Logger.info("[][revice][DeviceMessage:" + deviceMessage.getMessageId() + "]");
        eventPublisher.publishEvent(deviceMessage);
    }

    @Override
    public void receive(ServerStatusMessage mdmServerStatusMessage) {
        Logger.info("[][revice][MdmServerStatusMessage:" + mdmServerStatusMessage.getMessageId() + "]");
    }

    @Override
    public void receive(CommandMessage commandMessage) {
        Logger.info("[][revice][CommandMessage:" + commandMessage.getMessageId() + "]");
    }

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

}
