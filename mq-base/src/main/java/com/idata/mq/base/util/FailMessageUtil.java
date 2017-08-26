package com.idata.mq.base.util;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.idata.mq.base.message.BaseMessage;
import com.idata.mq.base.message.FailMessage;

public class FailMessageUtil {

    public static BaseMessage getMessage(FailMessage failMessage) {
        if (null != failMessage.getMessage()) {
            return failMessage.getMessage();
        }

        if (StringUtils.isNotBlank(failMessage.getClassName()) && StringUtils.isNotBlank(failMessage.getData())) {
            try {
                Object message = JSON.toJavaObject(JSON.parseObject(failMessage.getData()),
                        Class.forName(failMessage.getClassName()));
                return (BaseMessage) message;
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
