package com.idata.mq.base.message;

public class DeviceOfflineMessage extends BaseMessage {

    private String guid;

    public DeviceOfflineMessage() {
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Override
    public String toString() {
        StringBuilder buffer = builderString();
        buffer.append(";guid:").append(guid);
        try {
            return buffer.toString();
        }
        finally {
            buffer = null;
        }
    }
}
