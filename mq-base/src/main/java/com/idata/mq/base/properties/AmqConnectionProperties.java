package com.idata.mq.base.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AmqConnectionProperties {

    @Value("${conn.device.message.queue.name}")
    private String deviceMessageQueueName;

    @Value("${conn.device.message.routing.key}")
    private String deviceMessageRoutingKey;

    @Value("${conn.service.command.queue.name}")
    private String commandQueueName;

    @Value("${conn.service.command.routing.key}")
    private String commandRoutingKey;

    @Value("${conn.service.confirm.queue.name}")
    private String confirmQueueName;

    @Value("${conn.service.confirm.routing.key}")
    private String confirmRoutingKey;

    @Value("${conn.service.status.queue.name}")
    private String serviceStatusQueueName;

    @Value("${conn.service.status.routing.key}")
    private String serviceStatusRoutingKey;

    @Value("${conn.service.msg.fail.queue.name}")
    private String failMessageQueueName;

    @Value("${conn.service.msg.fail.routing.key}")
    private String failMsgRoutingKey;

    public String getDeviceMessageQueueName() {
        return deviceMessageQueueName;
    }

    public void setDeviceMessageQueueName(String deviceMessageQueueName) {
        this.deviceMessageQueueName = deviceMessageQueueName;
    }

    public String getDeviceMessageRoutingKey() {
        return deviceMessageRoutingKey;
    }

    public void setDeviceMessageRoutingKey(String deviceMessageRoutingKey) {
        this.deviceMessageRoutingKey = deviceMessageRoutingKey;
    }

    public String getCommandQueueName() {
        return commandQueueName;
    }

    public void setCommandQueueName(String commandQueueName) {
        this.commandQueueName = commandQueueName;
    }

    public String getCommandRoutingKey() {
        return commandRoutingKey;
    }

    public void setCommandRoutingKey(String commandRoutingKey) {
        this.commandRoutingKey = commandRoutingKey;
    }

    public String getConfirmQueueName() {
        return confirmQueueName;
    }

    public void setConfirmQueueName(String confirmQueueName) {
        this.confirmQueueName = confirmQueueName;
    }

    public String getConfirmRoutingKey() {
        return confirmRoutingKey;
    }

    public void setConfirmRoutingKey(String confirmRoutingKey) {
        this.confirmRoutingKey = confirmRoutingKey;
    }

    public String getServiceStatusQueueName() {
        return serviceStatusQueueName;
    }

    public void setServiceStatusQueueName(String serviceStatusQueueName) {
        this.serviceStatusQueueName = serviceStatusQueueName;
    }

    public String getServiceStatusRoutingKey() {
        return serviceStatusRoutingKey;
    }

    public void setServiceStatusRoutingKey(String serviceStatusRoutingKey) {
        this.serviceStatusRoutingKey = serviceStatusRoutingKey;
    }

    public String getFailMessageQueueName() {
        return failMessageQueueName;
    }

    public void setFailMessageQueueName(String failMessageQueueName) {
        this.failMessageQueueName = failMessageQueueName;
    }

    public String getFailMsgRoutingKey() {
        return failMsgRoutingKey;
    }

    public void setFailMsgRoutingKey(String failMsgRoutingKey) {
        this.failMsgRoutingKey = failMsgRoutingKey;
    }

}
