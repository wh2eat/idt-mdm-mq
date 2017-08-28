package com.idata.mq.mdm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idata.mq.base.constant.ServerConstants;
import com.idata.mq.base.exception.SendMessageException;
import com.idata.mq.base.message.DeviceMessageResultMessage;
import com.idata.mq.base.message.DeviceOfflineMessage;
import com.idata.mq.base.message.DeviceOnlineMessage;
import com.idata.mq.base.message.ServerStatusMessage;
import com.idata.mq.base.properties.AmpMdmProperties;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:mq-mdm-test-spring-context.xml" })
public class MdmListenerTest {

    private final static Logger logger = LogManager.getLogger(MdmListenerTest.class);

    @Autowired
    private MdmMessageReceiver mdmMessageReceiver;

    @Autowired
    private ConnectionMessageSender connectionMessageSender;

    @Before
    public void setUp() {
        mdmMessageReceiver.setMonitorServerName(ServerConstants.SERVER_NAME_CONNECTION);
    }

    @Test
    public void testReceiveDeviceOnlineMessage() throws SendMessageException, InterruptedException {
        if (logger.isDebugEnabled()) {
            logger.debug("[][testReceiveDeviceOnlineMessage][start]");
        }
        DeviceOnlineMessage onlineMessage = new DeviceOnlineMessage();
        onlineMessage.setMessageId("001");
        connectionMessageSender.sendMessage(ampMdmProperties.getDeviceOnlineRoutingKey(), onlineMessage);
        if (logger.isDebugEnabled()) {
            logger.debug("[][testReceiveDeviceOnlineMessage][sendDeviceOnline]");
        }

        Thread.currentThread();
        Thread.sleep(5 * 1000);

        DeviceOnlineMessage receiveDeviceOnlineMessage = mdmMessageReceiver.getReceiveDeviceOnlineMessage();
        Assert.assertNotNull(receiveDeviceOnlineMessage);
        Assert.assertEquals(onlineMessage.getMessageId(), receiveDeviceOnlineMessage.getMessageId());
        if (logger.isDebugEnabled()) {
            logger.debug("[][testReceiveDeviceOnlineMessage][success]");
        }
    }

    @Test
    public void testReceiveDeviceOfflineMessage() throws SendMessageException, InterruptedException {
        if (logger.isDebugEnabled()) {
            logger.debug("[][testReceiveDeviceOfflineMessage][start]");
        }
        DeviceOfflineMessage offlineMessage = new DeviceOfflineMessage();
        offlineMessage.setMessageId("002");
        connectionMessageSender.sendMessage(ampMdmProperties.getDeviceOfflineRoutingKey(), offlineMessage);
        if (logger.isDebugEnabled()) {
            logger.debug("[][testReceiveDeviceOfflineMessage][sendDeviceOffline]");
        }

        Thread.currentThread();
        Thread.sleep(3 * 1000);

        DeviceOfflineMessage receiveDeviceOfflineMessage = mdmMessageReceiver.getReceiveDeviceOfflineMessage();
        Assert.assertNotNull(receiveDeviceOfflineMessage);
        Assert.assertEquals(offlineMessage.getMessageId(), receiveDeviceOfflineMessage.getMessageId());
        if (logger.isDebugEnabled()) {
            logger.debug("[][testReceiveDeviceOfflineMessage][success]");
        }
    }

    @Test
    public void testReceiveDeviceMessageResultMessage() throws SendMessageException, InterruptedException {

        if (logger.isDebugEnabled()) {
            logger.debug("[][testReceiveDeviceMessageResultMessage][start]");
        }

        DeviceMessageResultMessage deviceMessageResultMessage = new DeviceMessageResultMessage();
        deviceMessageResultMessage.setDeviceMessageId("003");
        connectionMessageSender.sendMessage(ampMdmProperties.getDeviceMessageResultRoutingKey(),
                deviceMessageResultMessage);
        if (logger.isDebugEnabled()) {
            logger.debug("[][testReceiveDeviceMessageResultMessage][sendMessageResult]");
        }

        Thread.currentThread();
        Thread.sleep(3 * 1000);

        DeviceMessageResultMessage receiveResultMessage = mdmMessageReceiver.getReceiveDeviceMessageResultMessage();
        Assert.assertNotNull(receiveResultMessage);
        Assert.assertEquals(receiveResultMessage.getMessageId(), deviceMessageResultMessage.getMessageId());

        if (logger.isDebugEnabled()) {
            logger.debug("[][testReceiveDeviceMessageResultMessage][success]");
        }
    }

    @Test
    public void testReceiveServerStatusMessage() throws InterruptedException {
        if (logger.isDebugEnabled()) {
            logger.debug("[][testReceiveServerStatusMessage][start]");
        }

        ServerStatusMessage serverStatusMessage = new ServerStatusMessage();
        serverStatusMessage.setMessageId("004");
        serverStatusMessage.setServerName(ServerConstants.SERVER_NAME_CONNECTION);
        connectionMessageSender.sendMessage(ampMdmProperties.getServiceStatusRoutingKey(), serverStatusMessage);

        Thread.currentThread();
        Thread.sleep(5 * 1000);

        ServerStatusMessage receiveServerStatusMessage = mdmMessageReceiver.getReceiveServerStatusMessage();
        Assert.assertNotNull(receiveServerStatusMessage);
        Assert.assertEquals(ServerConstants.SERVER_NAME_CONNECTION, receiveServerStatusMessage.getServerName());
        if (logger.isDebugEnabled()) {
            logger.debug("[][testReceiveServerStatusMessage][success]");
        }
    }

    @Autowired
    private AmpMdmProperties ampMdmProperties;

}
