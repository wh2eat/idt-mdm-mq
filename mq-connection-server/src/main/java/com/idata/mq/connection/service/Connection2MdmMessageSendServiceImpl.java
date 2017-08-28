package com.idata.mq.connection.service;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.rabbit.support.ValueExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idata.mq.base.constant.ServerConstants;
import com.idata.mq.base.exception.SendMessageException;
import com.idata.mq.base.message.BaseMessage;
import com.idata.mq.base.message.ConfirmMessage;
import com.idata.mq.base.message.DeviceMessageResultMessage;
import com.idata.mq.base.message.DeviceOfflineMessage;
import com.idata.mq.base.message.DeviceOnlineMessage;
import com.idata.mq.base.message.FailMessage;
import com.idata.mq.base.message.ServerStatusMessage;
import com.idata.mq.base.properties.AmpMdmProperties;
import com.idata.mq.base.properties.AmqProperties;
import com.idata.mq.base.service.AbstractMessageSendService;
import com.idata.mq.base.service.MessageConfirmCallback;
import com.idata.mq.base.service.MessageReturnCallback;

@Service("connection2MdmMessageSendService")
public class Connection2MdmMessageSendServiceImpl extends AbstractMessageSendService
        implements Connection2MdmMessageSendService {

    private final static Logger logger = LogManager.getLogger(Connection2MdmMessageSendServiceImpl.class);

    @Autowired
    private RabbitTemplate connDirectTemplate;

    @Autowired
    public Connection2MdmMessageSendServiceImpl(AmqProperties amqProperties) {
        super(amqProperties, ServerConstants.SERVER_NAME_MDM_BUSINESS);
    }

    @Autowired
    private MessageConfirmCallback messageConfirmCallback;

    @Autowired
    private MessageReturnCallback returnCallback;

    @Override
    @PostConstruct
    public void init() {

        this.connDirectTemplate.setMandatory(true);

        this.connDirectTemplate.setMandatoryExpression(new ValueExpression<Boolean>(true));

        this.connDirectTemplate.setConfirmCallback(messageConfirmCallback);

        this.connDirectTemplate.setReturnCallback(returnCallback);

        super.init();

    }

    @Autowired
    private AmpMdmProperties amqMdmProperties;

    @Override
    public void sendDeviceOnline(DeviceOnlineMessage onlineMessage) throws SendMessageException {
        sendMessage(amqMdmProperties.getDeviceOnlineRoutingKey(), onlineMessage);
    }

    @Override
    public void sendDeviceOffline(DeviceOfflineMessage offlineMessage) throws SendMessageException {
        sendMessage(amqMdmProperties.getDeviceOfflineRoutingKey(), offlineMessage);
    }

    @Override
    public void sendMessageResult(String deviceMsgId, Integer result) throws SendMessageException {
        DeviceMessageResultMessage resultMessage = new DeviceMessageResultMessage();
        resultMessage.setDeviceMessageId(deviceMsgId);
        resultMessage.setResult(result);
        sendMessageResult(resultMessage);
    }

    private void sendMessageResult(DeviceMessageResultMessage resultMessage) throws SendMessageException {
        sendMessage(amqMdmProperties.getDeviceMessageResultRoutingKey(), resultMessage);
    }

    @Override
    public void sendAlive() throws SendMessageException {
        ServerStatusMessage statusMessage = new ServerStatusMessage();
        statusMessage.setStatus(ServerConstants.STATUS_RUNNING);
        statusMessage.setServerName(ServerConstants.SERVER_NAME_CONNECTION);
        sendAlive(statusMessage);
    }

    private void sendAlive(ServerStatusMessage statusMessage) throws SendMessageException {
        sendMessage(false, amqMdmProperties.getServiceStatusRoutingKey(), statusMessage);
    }

    @Override
    public void sendConfirmMessage(String messageId, Integer result) throws SendMessageException {
        ConfirmMessage message = new ConfirmMessage();
        message.setResult(1);
        message.setSourceMessageId(messageId);
        sendConfirmMessage(message);
    }

    private void sendConfirmMessage(ConfirmMessage confirmMessage) throws SendMessageException {
        sendMessage(amqMdmProperties.getConfirmRoutingKey(), confirmMessage);
    }

    @Override
    public void sendFailMessage(FailMessage failMessage) throws SendMessageException {
        sendMessage(amqMdmProperties.getFailMsgRoutingKey(), failMessage);
    }

    @Override
    protected void doResendMessage(BaseMessage message) {
        try {
            if (message instanceof DeviceMessageResultMessage) {
                sendMessageResult((DeviceMessageResultMessage) message);
            }
            else if (message instanceof ConfirmMessage) {
                sendConfirmMessage((ConfirmMessage) message);
            }
            else if (message instanceof ServerStatusMessage) {
                sendAlive((ServerStatusMessage) message);
            }
            else if (message instanceof DeviceOfflineMessage) {
                sendDeviceOffline((DeviceOfflineMessage) message);
            }
            else if (message instanceof DeviceOnlineMessage) {
                sendDeviceOnline((DeviceOnlineMessage) message);
            }
            else if (message instanceof FailMessage) {
                sendFailMessage((FailMessage) message);
            }
        }
        catch (SendMessageException e) {
            logger.error("[][doResendMessage][throws " + e.getClass().getName() + "]", e);
        }
    }

    @Override
    protected void doSendMessage(String routingKey, BaseMessage message, CorrelationData correlationData)
            throws SendMessageException {
        logger.info("[][doSendMessage][" + message.getClass().getSimpleName() + "][" + message.getMessageId() + "]");
        connDirectTemplate.convertAndSend(routingKey, message, correlationData);
    }

    public RabbitTemplate getConnDirectTemplate() {
        return connDirectTemplate;
    }

    public void setConnDirectTemplate(RabbitTemplate connDirectTemplate) {
        this.connDirectTemplate = connDirectTemplate;
    }

}
