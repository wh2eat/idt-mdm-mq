package com.idata.mq.conection.test;

import com.idata.mq.base.message.DeviceMessageResultMessage;
import com.idata.mq.base.message.DeviceOfflineMessage;
import com.idata.mq.base.message.DeviceOnlineMessage;
import com.idata.mq.base.receiver.DeviceMessageResultMessageReceiver;
import com.idata.mq.base.receiver.DeviceOfflineMessageReceiver;
import com.idata.mq.base.receiver.DeviceOnlineMessageReceiver;

public class MdmMessageReceiver
        implements DeviceOnlineMessageReceiver, DeviceOfflineMessageReceiver, DeviceMessageResultMessageReceiver {

    private DeviceMessageResultMessage receiveDeviceMessageResult;

    @Override
    public void receive(DeviceMessageResultMessage deviceMessageResultMessage) {
        this.receiveDeviceMessageResult = deviceMessageResultMessage;
    }

    private DeviceOfflineMessage receiveDeviceOfflineMessage;

    @Override
    public void receive(DeviceOfflineMessage deviceOfflineMessage) {
        this.receiveDeviceOfflineMessage = deviceOfflineMessage;
    }

    private DeviceOnlineMessage receiveDeviceOnlineMessage;

    @Override
    public void receive(DeviceOnlineMessage deviceOnlineMessage) {
        this.receiveDeviceOnlineMessage = deviceOnlineMessage;
    }

    public DeviceMessageResultMessage getReceiveDeviceMessageResult() {
        return receiveDeviceMessageResult;
    }

    public void setReceiveDeviceMessageResult(DeviceMessageResultMessage receiveDeviceMessageResult) {
        this.receiveDeviceMessageResult = receiveDeviceMessageResult;
    }

    public DeviceOfflineMessage getReceiveDeviceOfflineMessage() {
        return receiveDeviceOfflineMessage;
    }

    public void setReceiveDeviceOfflineMessage(DeviceOfflineMessage receiveDeviceOfflineMessage) {
        this.receiveDeviceOfflineMessage = receiveDeviceOfflineMessage;
    }

    public DeviceOnlineMessage getReceiveDeviceOnlineMessage() {
        return receiveDeviceOnlineMessage;
    }

    public void setReceiveDeviceOnlineMessage(DeviceOnlineMessage receiveDeviceOnlineMessage) {
        this.receiveDeviceOnlineMessage = receiveDeviceOnlineMessage;
    }

}
