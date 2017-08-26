package com.idata.mq;

import org.apache.commons.text.RandomStringGenerator;

import com.alibaba.fastjson.JSON;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.idata.mq.base.constant.ServerConstants;
import com.idata.mq.base.message.CommandMessage;
import com.idata.mq.base.message.DeviceMessage;
import com.idata.mq.base.message.DeviceMessageResultMessage;
import com.idata.mq.base.message.DeviceOfflineMessage;
import com.idata.mq.base.message.DeviceOnlineMessage;
import com.idata.mq.base.message.ServerStatusMessage;

public class MessageTest {

    public static void main(String[] args) {

        TimeBasedGenerator generator = Generators.timeBasedGenerator();

        DeviceMessage deviceMessage = new DeviceMessage();
        deviceMessage.setMessageId(generator.generate().toString());
        deviceMessage.setGuid(generator.generate().toString());

        RandomStringGenerator stringGenerator = new RandomStringGenerator.Builder().withinRange(97, 122).build();

        deviceMessage.setContent(stringGenerator.generate(64));
        deviceMessage.setContentId(stringGenerator.generate(32));

        System.out.println(JSON.toJSONString(deviceMessage));

        CommandMessage commandMessage = new CommandMessage();
        commandMessage.setMessageId(generator.generate().toString());
        commandMessage.setCommand("get-connection-status");
        commandMessage.setParameters(new String[] { "param1", "param2" });

        System.out.println(JSON.toJSONString(commandMessage));

        DeviceOfflineMessage offline = new DeviceOfflineMessage();
        offline.setMessageId(generator.generate().toString());
        offline.setGuid(generator.generate().toString());

        System.out.println(JSON.toJSONString(offline));

        DeviceOnlineMessage onlineMessage = new DeviceOnlineMessage();

        onlineMessage.setMessageId(generator.generate().toString());
        onlineMessage.setGuid(generator.generate().toString());
        onlineMessage.setIp("192.168.3.56");
        onlineMessage.setNetType("wifi");

        System.out.println(JSON.toJSONString(onlineMessage));

        DeviceMessageResultMessage resultMessage = new DeviceMessageResultMessage();
        resultMessage.setMessageId(generator.generate().toString());
        resultMessage.setDeviceMessageId(generator.generate().toString());
        resultMessage.setResult(1);
        System.out.println(JSON.toJSONString(resultMessage));

        ServerStatusMessage mdmServerStatusMessage = new ServerStatusMessage();
        mdmServerStatusMessage.setMessageId(generator.generate().toString());
        mdmServerStatusMessage.setStatus(1);
        mdmServerStatusMessage.setServerName(ServerConstants.SERVER_NAME_CONNECTION);
        System.out.println(JSON.toJSONString(mdmServerStatusMessage));

    }

}
