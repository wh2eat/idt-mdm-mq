package com.idata.mq.base.exception;

public class SendMessageException extends Exception {

    public final static String CODE_TARGET_SERVER_STOPED = "target_server_stoped";

    private String errorCode;

    public SendMessageException() {

    }

    public SendMessageException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public SendMessageException(String errorMessage) {
        super(errorMessage);
    }

    public SendMessageException(String errorMsg, Throwable e) {
        super(errorMsg, e);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
