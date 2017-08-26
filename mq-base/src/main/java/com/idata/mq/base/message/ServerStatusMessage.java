package com.idata.mq.base.message;

public class ServerStatusMessage extends BaseMessage {

    private Integer status;

    private String serverName;

    public ServerStatusMessage() {
        // TODO Auto-generated constructor stub
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String toString() {
        StringBuilder builder = builderString();
        builder.append("serverName:").append(serverName).append(";status:").append(status);
        try {
            return builder.toString();
        }
        finally {
            builder.toString();
        }
    }

}
