package com.idata.mq.conection.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.idata.mq.base.constant.ServerConstants;
import com.idata.mq.base.exception.SendMessageException;
import com.idata.mq.base.message.DeviceMessage;
import com.idata.mq.base.message.DeviceMessageResultMessage;
import com.idata.mq.base.message.DeviceOfflineMessage;
import com.idata.mq.base.message.DeviceOnlineMessage;
import com.idata.mq.base.message.FailMessage;
import com.idata.mq.base.message.ServerStatusMessage;
import com.idata.mq.base.util.FailMessageUtil;
import com.idata.mq.connection.service.Connection2MdmMessageSendService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:mq-connection-server-test-spring-context.xml" })
public class Connection2MdmMessageSendServiceTest {

    private final static Logger LOGGER = LogManager.getLogger(Connection2MdmMessageSendServiceTest.class);

    @Autowired
    private Connection2MdmMessageSendService connection2MdmMessageSendService;

    @Autowired
    private ConnectionMessageReceiver connectionMessageReceiver;

    @Autowired
    private MdmMessageReceiver mdmMessageReceiver;

    @Before
    public void init() {
        connectionMessageReceiver.setMonitorServerName(ServerConstants.SERVER_NAME_CONNECTION);
    }

    @Test
    public void testSendDeviceOnlineMessage() throws SendMessageException, InterruptedException {

        LOGGER.info("[][testSendDeviceOnlineMessage][start]");

        DeviceOnlineMessage deviceOnlineMessage = new DeviceOnlineMessage();
        deviceOnlineMessage.setMessageId("001");
        deviceOnlineMessage.setGuid("guid-001");
        connection2MdmMessageSendService.sendDeviceOnline(deviceOnlineMessage);
        LOGGER.info("[][testSendDeviceOnlineMessage][sendDeviceOnline]");

        Thread.currentThread();
        Thread.sleep(5 * 1000);

        DeviceOnlineMessage receiveDeviceOnlineMessage = mdmMessageReceiver.getReceiveDeviceOnlineMessage();
        Assert.assertNotNull(receiveDeviceOnlineMessage);
        Assert.assertEquals(deviceOnlineMessage.getMessageId(), receiveDeviceOnlineMessage.getMessageId());
        LOGGER.info("[][testSendDeviceOnlineMessage][success]");
    }

    @Test
    public void testSendDeviceOfflineMessage() throws SendMessageException, InterruptedException {

        LOGGER.info("[][testSendDeviceOfflineMessage][start]");

        DeviceOfflineMessage deviceOfflineMessage = new DeviceOfflineMessage();
        deviceOfflineMessage.setMessageId("002");
        deviceOfflineMessage.setGuid("guid-001");
        connection2MdmMessageSendService.sendDeviceOffline(deviceOfflineMessage);
        LOGGER.info("[][testSendDeviceOfflineMessage][sendDeviceOffline]");

        Thread.currentThread();
        Thread.sleep(5 * 1000);

        DeviceOfflineMessage receiveDeviceOfflineMessage = mdmMessageReceiver.getReceiveDeviceOfflineMessage();
        Assert.assertNotNull(receiveDeviceOfflineMessage);
        Assert.assertEquals(deviceOfflineMessage.getMessageId(), receiveDeviceOfflineMessage.getMessageId());
        LOGGER.info("[][testSendDeviceOnlineMessage][success]");
    }

    @Test
    public void testSendDeviceMessageResultMessage() throws SendMessageException, InterruptedException {

        LOGGER.info("[][testSendDeviceOfflineMessage][start]");

        String deviceMessageId = "003";
        connection2MdmMessageSendService.sendMessageResult(deviceMessageId, 1);
        LOGGER.info("[][testSendDeviceMessageResultMessage][sendMessageResult]");

        Thread.currentThread();
        Thread.sleep(5 * 1000);

        DeviceMessageResultMessage resultMessage = mdmMessageReceiver.getReceiveDeviceMessageResult();
        Assert.assertNotNull(resultMessage);
        Assert.assertEquals(deviceMessageId, resultMessage.getDeviceMessageId());
        LOGGER.info("[][testSendDeviceMessageResultMessage][success]");
    }

    @Test
    public void testSendAliveMessage() throws SendMessageException, InterruptedException {
        LOGGER.info("[][testSendAliveMessage][start]");
        connection2MdmMessageSendService.sendAlive();
        LOGGER.info("[][testSendAliveMessage][sendAlive]");
        Thread.currentThread();
        Thread.sleep(5 * 1000);
        ServerStatusMessage serverStatusMessage = connectionMessageReceiver.getReceiveServerStatusMessage();
        Assert.assertNotNull(serverStatusMessage);
        Assert.assertEquals(ServerConstants.SERVER_NAME_CONNECTION, serverStatusMessage.getServerName());
        LOGGER.info("[][testSendAliveMessage][success]");
    }

    @Test
    public void testSendFailMessage() throws SendMessageException, InterruptedException {

        DeviceMessage processFailMessage = new DeviceMessage();
        processFailMessage.setMessageId("006");
        processFailMessage.setContent("content");
        processFailMessage.setContentId("contentId");

        FailMessage failMessage = new FailMessage();
        failMessage.setClassName(processFailMessage.getClass().getName());
        failMessage.setData(JSON.toJSONString(processFailMessage));

        connection2MdmMessageSendService.sendFailMessage(failMessage);
        LOGGER.info("[][testSendFailMessage][sendFailMessage]");

        Thread.currentThread();
        Thread.sleep(3 * 1000);

        FailMessage reviceFailMessage = connectionMessageReceiver.getReceiveFailMessage();
        Assert.assertNotNull(reviceFailMessage);
        DeviceMessage reviceProcessFailMessage = (DeviceMessage) FailMessageUtil.getMessage(reviceFailMessage);

        Assert.assertEquals(reviceProcessFailMessage.getMessageId(), processFailMessage.getMessageId());
        LOGGER.info("[][testSendFailMessage][success]");
    }

}
