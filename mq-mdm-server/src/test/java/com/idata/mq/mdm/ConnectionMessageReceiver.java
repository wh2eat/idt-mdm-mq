package com.idata.mq.mdm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.idata.mq.base.message.CommandMessage;
import com.idata.mq.base.message.DeviceMessage;
import com.idata.mq.base.receiver.CommandMessageReceiver;
import com.idata.mq.base.receiver.DeviceMessageReceiver;

public class ConnectionMessageReceiver implements DeviceMessageReceiver, CommandMessageReceiver {

    private final static Logger LOGGER = LogManager.getLogger(ConnectionMessageReceiver.class);

    public ConnectionMessageReceiver() {
    }

    private CommandMessage receiveCommandMessage;

    @Override
    public void receive(CommandMessage commandMessage) {
        this.receiveCommandMessage = commandMessage;
    }

    private DeviceMessage receiveDeviceMessage;

    @Override
    public void receive(DeviceMessage deviceMessage) {
        this.receiveDeviceMessage = deviceMessage;
    }

    public CommandMessage getReceiveCommandMessage() {
        return receiveCommandMessage;
    }

    public void setReceiveCommandMessage(CommandMessage receiveCommandMessage) {
        this.receiveCommandMessage = receiveCommandMessage;
    }

    public DeviceMessage getReceiveDeviceMessage() {
        return receiveDeviceMessage;
    }

    public void setReceiveDeviceMessage(DeviceMessage receiveDeviceMessage) {
        this.receiveDeviceMessage = receiveDeviceMessage;
    }

}
