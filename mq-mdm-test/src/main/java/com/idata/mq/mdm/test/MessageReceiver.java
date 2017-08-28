package com.idata.mq.mdm.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.idata.mq.base.message.DeviceMessageResultMessage;
import com.idata.mq.base.message.DeviceOfflineMessage;
import com.idata.mq.base.message.DeviceOnlineMessage;
import com.idata.mq.base.message.FailMessage;
import com.idata.mq.base.message.ServerStatusMessage;
import com.idata.mq.base.util.FailMessageUtil;
import com.idata.mq.mdm.AbstractMessageReceiver;

public class MessageReceiver extends AbstractMessageReceiver {

    private final static Logger LOGGER = LogManager.getLogger(MessageReceiver.class);

    @Override
    public void receive(DeviceMessageResultMessage deviceMessageResultMessage) {
        LOGGER.info("[][receive][DeviceMessageResultMessage:" + deviceMessageResultMessage.getMessageId() + "]");
    }

    @Override
    public void receive(DeviceOnlineMessage deviceOnlineMessage) {
        LOGGER.info("[][receive][DeviceOnlineMessage:" + deviceOnlineMessage.getMessageId() + "]");
    }

    @Override
    public void receive(DeviceOfflineMessage deviceOfflineMessage) {
        LOGGER.info("[][receive][DeviceOfflineMessage:" + deviceOfflineMessage.getMessageId() + "]");
    }

    @Override
    public void receive(ServerStatusMessage connectionServerStatusMessage) {
        LOGGER.info("[][receive][ConnectionServerStatusMessage:" + connectionServerStatusMessage.getMessageId() + "]");
    }

    @Override
    public void receive(FailMessage failMessage) {
        LOGGER.info("[][receive][FailMessage:" + FailMessageUtil.getMessage(failMessage) + "]");
    }

}
