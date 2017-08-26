package com.idata.mq.base.event;

public class BackProcessFailedMessageEvent {

    private String dataClassName;

    private String data;

    public BackProcessFailedMessageEvent(String dataClassName, String data) {
        this.dataClassName = dataClassName;
        this.setData(data);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataClassName() {
        return dataClassName;
    }

    public void setDataClassName(String dataClassName) {
        this.dataClassName = dataClassName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("dataClassName:").append(dataClassName).append(";data:").append(data);
        try {
            return builder.toString();
        }
        finally {
            builder = null;
        }
    }

}
