package com.idata.mq.base.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class FailMessage extends BaseMessage {

    private String className;

    private String data;

    @JsonIgnoreProperties
    private Integer code;

    @JsonIgnoreProperties
    private BaseMessage message;

    public FailMessage() {

    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BaseMessage getMessage() {
        return message;
    }

    public void setMessage(BaseMessage message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
