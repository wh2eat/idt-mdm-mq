package com.idata.mq.base.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AmpMdmProperties {

    @Value("${mdm.device.online.queue.name}")
    private String deviceOnlineQueueName;

    @Value("${mdm.device.online.routing.key}")
    private String deviceOnlineRoutingKey;

    @Value("${mdm.device.offline.queue.name}")
    private String deviceOfflineQueueName;

    @Value("${mdm.device.offline.routing.key}")
    private String deviceOfflineRoutingKey;

    @Value("${mdm.device.message.result.queue.name}")
    private String deviceMessageResultQueueName;

    @Value("${mdm.device.message.result.routing.key}")
    private String deviceMessageResultRoutingKey;

    @Value("${mdm.service.confirm.queue.name}")
    private String confirmQueueName;

    @Value("${mdm.service.confirm.routing.key}")
    private String confirmRoutingKey;

    @Value("${mdm.service.status.queue.name}")
    private String serviceStatusQueueName;

    @Value("${mdm.service.status.routing.key}")
    private String serviceStatusRoutingKey;

    @Value("${mdm.service.msg.fail.queue.name}")
    private String failMsgQueueName;

    @Value("${mdm.service.msg.fail.routing.key}")
    private String failMsgRoutingKey;

    public String getDeviceOnlineQueueName() {
        return deviceOnlineQueueName;
    }

    public void setDeviceOnlineQueueName(String deviceOnlineQueueName) {
        this.deviceOnlineQueueName = deviceOnlineQueueName;
    }

    public String getDeviceOnlineRoutingKey() {
        return deviceOnlineRoutingKey;
    }

    public void setDeviceOnlineRoutingKey(String deviceOnlineRoutingKey) {
        this.deviceOnlineRoutingKey = deviceOnlineRoutingKey;
    }

    public String getDeviceOfflineQueueName() {
        return deviceOfflineQueueName;
    }

    public void setDeviceOfflineQueueName(String deviceOfflineQueueName) {
        this.deviceOfflineQueueName = deviceOfflineQueueName;
    }

    public String getDeviceOfflineRoutingKey() {
        return deviceOfflineRoutingKey;
    }

    public void setDeviceOfflineRoutingKey(String deviceOfflineRoutingKey) {
        this.deviceOfflineRoutingKey = deviceOfflineRoutingKey;
    }

    public String getDeviceMessageResultQueueName() {
        return deviceMessageResultQueueName;
    }

    public void setDeviceMessageResultQueueName(String deviceMessageResultQueueName) {
        this.deviceMessageResultQueueName = deviceMessageResultQueueName;
    }

    public String getDeviceMessageResultRoutingKey() {
        return deviceMessageResultRoutingKey;
    }

    public void setDeviceMessageResultRoutingKey(String deviceMessageResultRoutingKey) {
        this.deviceMessageResultRoutingKey = deviceMessageResultRoutingKey;
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

    public String getFailMsgQueueName() {
        return failMsgQueueName;
    }

    public void setFailMsgQueueName(String failMsgQueueName) {
        this.failMsgQueueName = failMsgQueueName;
    }

    public String getFailMsgRoutingKey() {
        return failMsgRoutingKey;
    }

    public void setFailMsgRoutingKey(String failMsgRoutingKey) {
        this.failMsgRoutingKey = failMsgRoutingKey;
    }

}
