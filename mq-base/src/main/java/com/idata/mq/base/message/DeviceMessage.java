package com.idata.mq.base.message;

public class DeviceMessage extends BaseMessage {

    private String guid;

    private String contentId;

    // utf-8 encoding
    // base64
    private String content;

    public DeviceMessage() {
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuilder buffer = builderString();
        buffer.append(";guid:").append(guid).append(";content:").append(content);
        try {
            return buffer.toString();
        }
        finally {
            buffer = null;
        }
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
}
