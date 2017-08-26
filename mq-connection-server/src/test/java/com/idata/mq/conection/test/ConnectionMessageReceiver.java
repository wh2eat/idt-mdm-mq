package com.idata.mq.conection.test;

import com.idata.mq.base.message.CommandMessage;
import com.idata.mq.base.message.DeviceMessage;
import com.idata.mq.base.message.FailMessage;
import com.idata.mq.base.message.ServerStatusMessage;
import com.idata.mq.connection.AbstractMessageReceiver;

public class ConnectionMessageReceiver extends AbstractMessageReceiver {

    private String monitorServerName;

    private CommandMessage receiveCommandMessage;

    @Override
    public void receive(CommandMessage commandMessage) {
        this.receiveCommandMessage = commandMessage;
    }

    private ServerStatusMessage receiveServerStatusMessage;

    @Override
    public void receive(ServerStatusMessage serverStatusMessage) {
        if (null == monitorServerName || monitorServerName.equals(serverStatusMessage.getServerName())) {
            this.receiveServerStatusMessage = serverStatusMessage;
        }
    }

    private DeviceMessage receiveDeviceMessage;

    @Override
    public void receive(DeviceMessage deviceMessage) {
        this.receiveDeviceMessage = deviceMessage;
    }

    private FailMessage receiveFailMessage;

    @Override
    public void receive(FailMessage failMessage) {
        this.receiveFailMessage = failMessage;
    }

    public CommandMessage getReceiveCommandMessage() {
        return receiveCommandMessage;
    }

    public void setReceiveCommandMessage(CommandMessage receiveCommandMessage) {
        this.receiveCommandMessage = receiveCommandMessage;
    }

    public ServerStatusMessage getReceiveServerStatusMessage() {
        return receiveServerStatusMessage;
    }

    public void setReceiveServerStatusMessage(ServerStatusMessage receiveServerStatusMessage) {
        this.receiveServerStatusMessage = receiveServerStatusMessage;
    }

    public DeviceMessage getReceiveDeviceMessage() {
        return receiveDeviceMessage;
    }

    public void setReceiveDeviceMessage(DeviceMessage receiveDeviceMessage) {
        this.receiveDeviceMessage = receiveDeviceMessage;
    }

    public FailMessage getReceiveFailMessage() {
        return receiveFailMessage;
    }

    public void setReceiveFailMessage(FailMessage receiveFailMessage) {
        this.receiveFailMessage = receiveFailMessage;
    }

    public String getMonitorServerName() {
        return monitorServerName;
    }

    public void setMonitorServerName(String monitorServerName) {
        this.monitorServerName = monitorServerName;
    }

}
