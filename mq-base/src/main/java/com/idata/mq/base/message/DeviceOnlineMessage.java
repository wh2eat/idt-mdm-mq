package com.idata.mq.base.message;

public class DeviceOnlineMessage extends BaseMessage {

    private String guid;

    private String ip;

    private String netType;

    private String netName;

    public DeviceOnlineMessage() {
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    @Override
    public String toString() {
        StringBuilder buffer = builderString();
        buffer.append(";guid:").append(guid).append(";ip:").append(ip).append(";netType:").append(netType);
        try {
            return buffer.toString();
        }
        finally {
            buffer = null;
        }
    }

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }
}
