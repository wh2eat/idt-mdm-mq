package com.idata.mq.base.message;

public class DeviceMessageResultMessage extends BaseMessage {

    private String deviceMessageId;

    private Integer result;

    public DeviceMessageResultMessage() {
        // TODO Auto-generated constructor stub
    }

    public String getDeviceMessageId() {
        return deviceMessageId;
    }

    public void setDeviceMessageId(String deviceMessageId) {
        this.deviceMessageId = deviceMessageId;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @Override
    public String toString() {
        StringBuilder buffer = builderString();
        buffer.append(";deviceMessageId:").append(deviceMessageId).append(";result:").append(result);
        try {
            return buffer.toString();
        }
        finally {
            buffer = null;
        }
    }

}
