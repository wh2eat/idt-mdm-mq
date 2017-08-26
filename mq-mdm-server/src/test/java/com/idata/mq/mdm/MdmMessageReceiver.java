package com.idata.mq.mdm;

import com.idata.mq.base.message.DeviceMessageResultMessage;
import com.idata.mq.base.message.DeviceOfflineMessage;
import com.idata.mq.base.message.DeviceOnlineMessage;
import com.idata.mq.base.message.FailMessage;
import com.idata.mq.base.message.ServerStatusMessage;

public class MdmMessageReceiver extends AbstractMessageReceiver {

    private DeviceMessageResultMessage receiveDeviceMessageResultMessage;

    @Override
    public void receive(DeviceMessageResultMessage deviceMessageResultMessage) {
        this.receiveDeviceMessageResultMessage = deviceMessageResultMessage;
    }

    private DeviceOnlineMessage receiveDeviceOnlineMessage;

    @Override
    public void receive(DeviceOnlineMessage deviceOnlineMessage) {
        this.receiveDeviceOnlineMessage = deviceOnlineMessage;
    }

    private DeviceOfflineMessage receiveDeviceOfflineMessage;

    @Override
    public void receive(DeviceOfflineMessage deviceOfflineMessage) {
        this.receiveDeviceOfflineMessage = deviceOfflineMessage;
    }

    private String monitorServerName;

    private ServerStatusMessage receiveServerStatusMessage;

    @Override
    public void receive(ServerStatusMessage serverStatusMessage) {
        if (null == monitorServerName || monitorServerName.equals(serverStatusMessage.getServerName())) {
            this.receiveServerStatusMessage = serverStatusMessage;
        }
    }

    private FailMessage receiveFailMessage;

    @Override
    public void receive(FailMessage failMessage) {
        this.receiveFailMessage = failMessage;
    }

    public DeviceMessageResultMessage getReceiveDeviceMessageResultMessage() {
        return receiveDeviceMessageResultMessage;
    }

    public void setReceiveDeviceMessageResultMessage(DeviceMessageResultMessage receiveDeviceMessageResultMessage) {
        this.receiveDeviceMessageResultMessage = receiveDeviceMessageResultMessage;
    }

    public DeviceOnlineMessage getReceiveDeviceOnlineMessage() {
        return receiveDeviceOnlineMessage;
    }

    public void setReceiveDeviceOnlineMessage(DeviceOnlineMessage receiveDeviceOnlineMessage) {
        this.receiveDeviceOnlineMessage = receiveDeviceOnlineMessage;
    }

    public DeviceOfflineMessage getReceiveDeviceOfflineMessage() {
        return receiveDeviceOfflineMessage;
    }

    public void setReceiveDeviceOfflineMessage(DeviceOfflineMessage receiveDeviceOfflineMessage) {
        this.receiveDeviceOfflineMessage = receiveDeviceOfflineMessage;
    }

    public String getMonitorServerName() {
        return monitorServerName;
    }

    public void setMonitorServerName(String monitorServerName) {
        this.monitorServerName = monitorServerName;
    }

    public ServerStatusMessage getReceiveServerStatusMessage() {
        return receiveServerStatusMessage;
    }

    public void setReceiveServerStatusMessage(ServerStatusMessage receiveServerStatusMessage) {
        this.receiveServerStatusMessage = receiveServerStatusMessage;
    }

    public FailMessage getReceiveFailMessage() {
        return receiveFailMessage;
    }

    public void setReceiveFailMessage(FailMessage receiveFailMessage) {
        this.receiveFailMessage = receiveFailMessage;
    }

}
