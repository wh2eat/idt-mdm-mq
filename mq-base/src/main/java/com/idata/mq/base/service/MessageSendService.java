package com.idata.mq.base.service;

import com.idata.mq.base.exception.SendMessageException;
import com.idata.mq.base.message.FailMessage;

public interface MessageSendService {

    int MESSAGE_CONFIRM_RESULT_SUCCESS = 1;

    int MESSAGE_CONFIRM_RESULT_FAILED = 0;

    // 发送已接受到消息
    void sendConfirmMessage(String messageId, Integer result) throws SendMessageException;

    void sendFailMessage(FailMessage failMessage) throws SendMessageException;

    void sendAlive() throws SendMessageException;

}
