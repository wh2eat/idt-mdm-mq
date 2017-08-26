package com.idata.mq.mdm.service;

import com.idata.mq.base.exception.SendMessageException;
import com.idata.mq.base.message.CommandMessage;
import com.idata.mq.base.message.DeviceMessage;
import com.idata.mq.base.service.MessageSendService;

public interface Mdm2ConnectionMessageSendServce extends MessageSendService {

    void sendDeviceMessage(DeviceMessage deviceMessage) throws SendMessageException;

    void sendCommandMessage(CommandMessage commandMessage) throws SendMessageException;

}
