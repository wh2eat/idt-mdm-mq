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

import com.idata.mq.base.constant.CommandConstants;
import com.idata.mq.base.constant.ServerConstants;
import com.idata.mq.base.exception.SendMessageException;
import com.idata.mq.base.message.CommandMessage;
import com.idata.mq.base.message.DeviceMessage;
import com.idata.mq.base.message.ServerStatusMessage;
import com.idata.mq.base.properties.AmpMdmProperties;
import com.idata.mq.base.properties.AmqProperties;
import com.idata.mq.mdm.service.Mdm2ConnectionMessageSendServce;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:mq-mdm-test-spring-context.xml" })
public class Mdm2ConnectionMessageSendServiceTest {

    private final static Logger LOGGER = LogManager.getLogger(Mdm2ConnectionMessageSendServiceTest.class);

    @Autowired
    private Mdm2ConnectionMessageSendServce mdm2ConnectionMessageSendServce;

    @Autowired
    private ConnectionMessageReceiver connectionMessageReceiver;

    @Autowired
    private MdmMessageReceiver mdmMessageReceiver;

    @Before
    public void setUp() {
        mdmMessageReceiver.setMonitorServerName(ServerConstants.SERVER_NAME_MDM_BUSINESS);
    }

    @Test
    public void testSendDeviceMessage() throws SendMessageException, InterruptedException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][testSendDeviceMessage][]");
        }
        DeviceMessage deviceMessage = new DeviceMessage();
        deviceMessage.setMessageId("001");
        mdm2ConnectionMessageSendServce.sendDeviceMessage(deviceMessage);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][testSendDeviceMessage][sendDeviceMessage]");
        }

        Thread.currentThread();
        Thread.sleep(3 * 1000);

        DeviceMessage receiveDeviceMessage = connectionMessageReceiver.getReceiveDeviceMessage();
        Assert.assertNotNull(receiveDeviceMessage);
        Assert.assertEquals(receiveDeviceMessage.getMessageId(), deviceMessage.getMessageId());
        LOGGER.info("[][testSendDeviceMessage][success]");
    }

    @Test
    public void testSendCommandMessage() throws SendMessageException, InterruptedException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][testSendCommandMessage][start]");
        }
        CommandMessage commandMessage = new CommandMessage();
        commandMessage.setMessageId("002");
        commandMessage.setCommand(CommandConstants.GET_CONNECTION_SERVER_STATUS);
        mdm2ConnectionMessageSendServce.sendCommandMessage(commandMessage);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][testSendCommandMessage][sendCommandMessage]");
        }
        Thread.currentThread();
        Thread.sleep(5 * 1000);

        CommandMessage receiveCommandMessage = connectionMessageReceiver.getReceiveCommandMessage();
        Assert.assertNotNull(receiveCommandMessage);
        Assert.assertEquals(receiveCommandMessage.getMessageId(), commandMessage.getMessageId());
        LOGGER.info("[][testSendCommandMessage][success]");
    }

    @Autowired
    private AmqProperties amqProperties;

    @Test
    public void testSendAliveMessage() throws SendMessageException, InterruptedException {
        amqProperties.setAutoSendAlive(false);
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][testSendAliveMessage][start]");
            }
            mdm2ConnectionMessageSendServce.sendAlive();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][testSendAliveMessage][sendAlive]");
            }

            Thread.currentThread();
            Thread.sleep(5 * 1000);

            ServerStatusMessage receiveServerStatusMessage = mdmMessageReceiver.getReceiveServerStatusMessage();
            Assert.assertNotNull(receiveServerStatusMessage);
            Assert.assertEquals(ServerConstants.SERVER_NAME_MDM_BUSINESS, receiveServerStatusMessage.getServerName());
            LOGGER.info("[][testSendAliveMessage][success]");
        }
        finally {
            amqProperties.setAutoSendAlive(true);
        }
    }

    @Autowired
    private ConnectionMessageSender connectionMessageSender;

    @Autowired
    private AmpMdmProperties ampMdmProperties;

    @Test
    public void testTargetServerStoped() throws InterruptedException, SendMessageException {
        long aliveSendMillis = amqProperties.getAliveSendMillis();
        long aliveTimeoutMillis = amqProperties.getAliveTimeoutMillis();
        amqProperties.setAutoSendAlive(false);

        try {
            long newSendMillis = 10 * 1000;
            amqProperties.setAliveSendMillis(newSendMillis);
            long newTimeoutMillis = 20 * 1000;
            amqProperties.setAliveTimeoutMillis(newTimeoutMillis);

            Thread.currentThread();
            Thread.sleep(aliveSendMillis + newSendMillis);

            CommandMessage commandMessage = new CommandMessage();
            try {
                mdm2ConnectionMessageSendServce.sendCommandMessage(commandMessage);
            }
            catch (SendMessageException e) {
                String errorCode = e.getErrorCode();
                Assert.assertEquals(SendMessageException.CODE_TARGET_SERVER_STOPED, errorCode);
                LOGGER.info("[][testTargetServerStoped][test stop success]");
            }

            ServerStatusMessage serverStatusMessage = new ServerStatusMessage();
            serverStatusMessage.setMessageId("0007");
            serverStatusMessage.setServerName(ServerConstants.SERVER_NAME_CONNECTION);
            serverStatusMessage.setStatus(ServerConstants.STATUS_RUNNING);
            connectionMessageSender.sendMessage(ampMdmProperties.getServiceStatusRoutingKey(), serverStatusMessage);

            Thread.currentThread();
            Thread.sleep(5 * 1000);

            commandMessage.setMessageId("005");
            mdm2ConnectionMessageSendServce.sendCommandMessage(commandMessage);

            Thread.currentThread();
            Thread.sleep(5 * 1000);

            CommandMessage receiveCommandMessage = connectionMessageReceiver.getReceiveCommandMessage();
            Assert.assertNotNull(receiveCommandMessage);
            Assert.assertEquals(receiveCommandMessage.getMessageId(), commandMessage.getMessageId());

            LOGGER.info("[][testTargetServerStoped][success]");
        }
        finally {
            amqProperties.setAutoSendAlive(true);
            amqProperties.setAliveSendMillis(aliveSendMillis);
            amqProperties.setAliveTimeoutMillis(aliveTimeoutMillis);
        }
    }

    @Test
    public void testAutoSendAlive() throws InterruptedException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][testAutoSendAlive][start]");
        }

        long aliveSendMillis = amqProperties.getAliveSendMillis();

        mdmMessageReceiver.setReceiveServerStatusMessage(null);
        amqProperties.setAutoSendAlive(true);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][][]");
        }

        Thread.currentThread();
        Thread.sleep(aliveSendMillis + 1000);

        ServerStatusMessage receiveServerStatusMessage = mdmMessageReceiver.getReceiveServerStatusMessage();
        Assert.assertNotNull(receiveServerStatusMessage);
        Assert.assertEquals(receiveServerStatusMessage.getServerName(), ServerConstants.SERVER_NAME_MDM_BUSINESS);
        LOGGER.info("[][testAutoSendAlive][success]");

    }

}
