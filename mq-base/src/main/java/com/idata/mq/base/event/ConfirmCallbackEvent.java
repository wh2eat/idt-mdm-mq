package com.idata.mq.base.event;

public class ConfirmCallbackEvent {

    private String correlationId;

    private boolean ack;

    public ConfirmCallbackEvent(String correlationId, boolean ack) {
        this.setCorrelationId(correlationId);
        this.setAck(ack);
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public boolean isAck() {
        return ack;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }

    @Override
    public String toString() {
        return "correlationId:" + correlationId + ";ack:" + ack;
    }

}
