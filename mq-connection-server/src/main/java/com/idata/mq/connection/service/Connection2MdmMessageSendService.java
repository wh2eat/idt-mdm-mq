package com.idata.mq.connection.service;

import com.idata.mq.base.exception.SendMessageException;
import com.idata.mq.base.message.DeviceOfflineMessage;
import com.idata.mq.base.message.DeviceOnlineMessage;
import com.idata.mq.base.service.MessageSendService;

public interface Connection2MdmMessageSendService extends MessageSendService {

    void sendDeviceOnline(DeviceOnlineMessage onlineMessage) throws SendMessageException;

    void sendDeviceOffline(DeviceOfflineMessage offlineMessage) throws SendMessageException;

    void sendMessageResult(String msgId, Integer result) throws SendMessageException;

}
