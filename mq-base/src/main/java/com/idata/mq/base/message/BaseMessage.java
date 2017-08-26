package com.idata.mq.base.message;

public class BaseMessage {

    private String messageId;

    // @JSONField(serialize = false, deserialize = false)
    // private long sendMillis = System.currentTimeMillis();
    //
    // @JSONField(serialize = false, deserialize = false)
    // private int resendTimes = 1;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    // public long getSendMillis() {
    // return sendMillis;
    // }
    //
    // public void setSendMillis(long sendMillis) {
    // this.sendMillis = sendMillis;
    // }
    //
    // public int getResendTimes() {
    // return resendTimes;
    // }
    //
    // public void setResendTimes(int resendTimes) {
    // this.resendTimes = resendTimes;
    // }

    protected StringBuilder builderString() {
        StringBuilder builder = new StringBuilder();
        builder.append("messageId:").append(messageId)
        // .append(";resendTimes:").append(resendTimes)
        // .append(";sendMillis:").append(sendMillis)
        ;
        return builder;
    }

}
