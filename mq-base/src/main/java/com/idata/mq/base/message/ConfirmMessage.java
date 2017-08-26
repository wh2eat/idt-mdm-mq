package com.idata.mq.base.message;

public class ConfirmMessage extends BaseMessage {

    private Integer result;

    private String sourceMessageId;

    public ConfirmMessage() {

    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getSourceMessageId() {
        return sourceMessageId;
    }

    public void setSourceMessageId(String sourceMessageId) {
        this.sourceMessageId = sourceMessageId;
    }

    @Override
    public String toString() {
        StringBuilder buffer = builderString();
        buffer.append(";sourceMessageId:").append(sourceMessageId).append(";result:").append(result);
        try {
            return buffer.toString();
        }
        finally {
            buffer = null;
        }
    }
}
