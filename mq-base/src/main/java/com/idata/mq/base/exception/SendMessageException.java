package com.idata.mq.base.exception;

public class SendMessageException extends Exception {

    public SendMessageException() {

    }

    public SendMessageException(String errorMessage) {
        super(errorMessage);
    }

    public SendMessageException(String errorMsg, Throwable e) {
        super(errorMsg, e);
    }
}
