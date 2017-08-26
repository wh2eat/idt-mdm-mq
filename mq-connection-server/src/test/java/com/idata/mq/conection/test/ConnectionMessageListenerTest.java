package com.idata.mq.conection.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.idata.mq.base.constant.CommandConstants;
import com.idata.mq.base.constant.ServerConstants;
import com.idata.mq.base.exception.SendMessageException;
import com.idata.mq.base.message.BaseMessage;
import com.idata.mq.base.message.CommandMessage;
import com.idata.mq.base.message.DeviceMessage;
import com.idata.mq.base.message.FailMessage;
import com.idata.mq.base.message.ServerStatusMessage;
import com.idata.mq.base.properties.AmqConnectionProperties;
import com.idata.mq.base.util.FailMessageUtil;
import com.idata.mq.base.util.RandomGeneratorUtil;

//mq-connection-listener-test-spring-context.xml
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:mq-connection-server-test-spring-context.xml" })
public class ConnectionMessageListenerTest {

    private final static Logger logger = LogManager.getLogger(ConnectionMessageListenerTest.class);

    @Autowired
    private ConnectionMessageReceiver connectionMessageReceiver;

    @Autowired
    private RabbitTemplate mdmDirectTemplate;

    @Autowired
    private AmqConnectionProperties amqConnectionProperties;

    private TimeBasedGenerator generator = RandomGeneratorUtil.getTimeBasedGenerator();

    @Before
    public void init() throws SendMessageException {
        connectionMessageReceiver.setMonitorServerName(ServerConstants.SERVER_NAME_MDM_BUSINESS);
    }

    private void send(String routingKey, BaseMessage message) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(generator.generate().toString());
        mdmDirectTemplate.convertAndSend(routingKey, message, correlationData);
    }

    @Test
    public void testReceiveCommandMessage() throws SendMessageException, InterruptedException {

        logger.info("[][testReceiveCommandMessage][start]");

        CommandMessage commandMessage = new CommandMessage();
        commandMessage.setMessageId("001");
        commandMessage.setCommand(CommandConstants.GET_DEVICE_ONLINE_STATUS);
        send(amqConnectionProperties.getCommandRoutingKey(), commandMessage);

        logger.info("[][testReceiveCommandMessage][sendCommandMessage finish]");

        Thread.currentThread();
        Thread.sleep(5 * 1000);

        CommandMessage receiveCommandMessage = connectionMessageReceiver.getReceiveCommandMessage();

        Assert.assertNotNull(receiveCommandMessage);
        Assert.assertEquals(commandMessage.getMessageId(), receiveCommandMessage.getMessageId());

        logger.info("[][init][testReceiveCommandMessage success ]");
    }

    @Test
    public void testReceiveDeviceMessage() throws SendMessageException, InterruptedException {

        logger.info("[][testReceiveDeviceMessage][start]");

        DeviceMessage deviceMessage = new DeviceMessage();
        deviceMessage.setMessageId("002");
        deviceMessage.setGuid("guid-0001dfdf-dfdfd444df");

        send(amqConnectionProperties.getDeviceMessageRoutingKey(), deviceMessage);

        logger.info("[][init][sendDeviceMessage finish]");
        logger.info("[][testReceiveDeviceMessage][sendCommandMessage finish]");

        Thread.currentThread();
        Thread.sleep(5 * 1000);

        DeviceMessage receiveCommandMessage = connectionMessageReceiver.getReceiveDeviceMessage();

        Assert.assertNotNull(receiveCommandMessage);
        Assert.assertEquals(deviceMessage.getMessageId(), receiveCommandMessage.getMessageId());

        logger.info("[][init][testReceiveDeviceMessage success ]");
    }

    @Test
    public void testReceiveFailMessage() throws SendMessageException, InterruptedException {

        logger.info("[][testReceiveFailMessage][start]");

        CommandMessage processFailMessage = new CommandMessage();
        processFailMessage.setMessageId("003");
        processFailMessage.setCommand(CommandConstants.GET_DEVICE_MESSAGE_RESULT);

        FailMessage failMessage = new FailMessage();
        failMessage.setClassName(processFailMessage.getClass().getName());
        failMessage.setData(JSON.toJSONString(processFailMessage));
        send(amqConnectionProperties.getFailMsgRoutingKey(), failMessage);
        logger.info("[][testReceiveFailMessage][sendFailMessage finish]");

        Thread.currentThread();
        Thread.sleep(5 * 1000);

        FailMessage receiveFailMessage = connectionMessageReceiver.getReceiveFailMessage();

        Assert.assertNotNull(receiveFailMessage);

        BaseMessage failBaseMessage = FailMessageUtil.getMessage(receiveFailMessage);
        Assert.assertNotNull(failBaseMessage);
        CommandMessage failCommandMessage = (CommandMessage) failBaseMessage;
        Assert.assertEquals(failCommandMessage.getMessageId(), processFailMessage.getMessageId());

        logger.info("[][testReceiveFailMessage][success ]");
    }

    @Test
    public void testReceiveAliveMessage() throws SendMessageException, InterruptedException {

        logger.info("[][testReceiveAliveMessage][start]");

        ServerStatusMessage statusMessage = new ServerStatusMessage();
        statusMessage.setMessageId("004");
        statusMessage.setServerName(ServerConstants.SERVER_NAME_MDM_BUSINESS);

        send(amqConnectionProperties.getServiceStatusRoutingKey(), statusMessage);

        Thread.currentThread();
        Thread.sleep(5 * 1000);

        ServerStatusMessage receiveServerStatusMessage = connectionMessageReceiver.getReceiveServerStatusMessage();

        Assert.assertNotNull(receiveServerStatusMessage);

        Assert.assertEquals(ServerConstants.SERVER_NAME_MDM_BUSINESS, receiveServerStatusMessage.getServerName());

        logger.info("[][testReceiveAliveMessage][success ]");
    }

}
