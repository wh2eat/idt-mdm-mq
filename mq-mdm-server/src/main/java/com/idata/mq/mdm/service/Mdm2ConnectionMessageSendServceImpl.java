package com.idata.mq.mdm.service;

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
import com.idata.mq.base.message.CommandMessage;
import com.idata.mq.base.message.ConfirmMessage;
import com.idata.mq.base.message.DeviceMessage;
import com.idata.mq.base.message.FailMessage;
import com.idata.mq.base.message.ServerStatusMessage;
import com.idata.mq.base.properties.AmqConnectionProperties;
import com.idata.mq.base.properties.AmqProperties;
import com.idata.mq.base.service.AbstractMessageSendService;
import com.idata.mq.base.service.MessageConfirmCallback;
import com.idata.mq.base.service.MessageReturnCallback;

@Service
public class Mdm2ConnectionMessageSendServceImpl extends AbstractMessageSendService
        implements Mdm2ConnectionMessageSendServce {

    private final static Logger LOGGER = LogManager.getLogger(Mdm2ConnectionMessageSendServceImpl.class);

    @Autowired
    private RabbitTemplate mdmDirectTemplate;

    @Autowired
    public Mdm2ConnectionMessageSendServceImpl(AmqProperties amqProperties) {
        super(amqProperties, ServerConstants.SERVER_NAME_CONNECTION);
    }

    @Autowired
    private MessageConfirmCallback messageConfirmCallback;

    @Autowired
    private MessageReturnCallback returnCallback;

    @Override
    @PostConstruct
    public void init() {

        this.mdmDirectTemplate.setMandatory(true);

        this.mdmDirectTemplate.setMandatoryExpression(new ValueExpression<Boolean>(true));

        this.mdmDirectTemplate.setConfirmCallback(messageConfirmCallback);

        this.mdmDirectTemplate.setReturnCallback(returnCallback);

        super.init();
    }

    @Autowired
    private AmqConnectionProperties amqConnectionProperties;

    @Override
    public void sendDeviceMessage(DeviceMessage deviceMessageVo) throws SendMessageException {
        sendMessage(amqConnectionProperties.getDeviceMessageRoutingKey(), deviceMessageVo);
    }

    @Override
    public void sendCommandMessage(CommandMessage commandMessageVo) throws SendMessageException {
        sendMessage(amqConnectionProperties.getCommandRoutingKey(), commandMessageVo);
    }

    @Override
    public void sendConfirmMessage(String msgId, Integer result) throws SendMessageException {
        ConfirmMessage confirmMessage = new ConfirmMessage();
        confirmMessage.setSourceMessageId(msgId);
        confirmMessage.setResult(result);
        sendConfirmMessage(confirmMessage);
    }

    private void sendConfirmMessage(ConfirmMessage confirmMessage) throws SendMessageException {
        sendMessage(amqConnectionProperties.getConfirmRoutingKey(), confirmMessage);
    }

    @Override
    public void sendAlive() throws SendMessageException {
        ServerStatusMessage statusMessage = new ServerStatusMessage();
        statusMessage.setStatus(ServerConstants.STATUS_RUNNING);
        statusMessage.setServerName(ServerConstants.SERVER_NAME_MDM_BUSINESS);
        sendAlive(statusMessage);
    }

    private void sendAlive(ServerStatusMessage statusMessage) throws SendMessageException {
        sendMessage(amqConnectionProperties.getServiceStatusRoutingKey(), statusMessage);
    }

    @Override
    public void sendFailMessage(FailMessage failMessage) throws SendMessageException {
        sendMessage(amqConnectionProperties.getFailMsgRoutingKey(), failMessage);
    }

    @Override
    protected void doSendMessage(String routingKey, BaseMessage message, CorrelationData correlationData)
            throws SendMessageException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][send][" + message.getClass().getSimpleName() + "][" + message.getMessageId() + "]");
        }
        mdmDirectTemplate.convertAndSend(routingKey, message, correlationData);
    }

    @Override
    protected void doResendMessage(BaseMessage baseMessage) throws SendMessageException {
        if (baseMessage instanceof DeviceMessage) {
            sendDeviceMessage((DeviceMessage) baseMessage);
        }
        else if (baseMessage instanceof CommandMessage) {
            sendCommandMessage((CommandMessage) baseMessage);
        }
        else if (baseMessage instanceof ConfirmMessage) {
            sendConfirmMessage((ConfirmMessage) baseMessage);
        }
        else if (baseMessage instanceof ServerStatusMessage) {
            sendAlive((ServerStatusMessage) baseMessage);
        }
        else if (baseMessage instanceof FailMessage) {
            sendFailMessage((FailMessage) baseMessage);
        }
    }

    public RabbitTemplate getMdmDirectTemplate() {
        return mdmDirectTemplate;
    }

    public void setMdmDirectTemplate(RabbitTemplate mdmDirectTemplate) {
        this.mdmDirectTemplate = mdmDirectTemplate;
    }

}
