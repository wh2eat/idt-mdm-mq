package com.idata.mq;

import com.alibaba.fastjson.JSON;
import com.idata.mq.base.message.BaseMessage;
import com.idata.mq.base.message.DeviceMessage;

public class JsonSerialTest {

    public static void main(String[] args) {
        BaseMessage message = new BaseMessage();
        message.setMessageId("dfd");
        // message.setResendTimes(1);
        // message.setSendMillis(System.currentTimeMillis());

        System.out.println(JSON.toJSONString(message));

        DeviceMessage deviceMessage = new DeviceMessage();
        // deviceMessage.setResendTimes(1);
        // deviceMessage.setSendMillis(System.currentTimeMillis());
        deviceMessage.setGuid("dfdfdf");
        deviceMessage.setContent("dfdf");

        System.out.println(JSON.toJSONString(deviceMessage));

    }

}
