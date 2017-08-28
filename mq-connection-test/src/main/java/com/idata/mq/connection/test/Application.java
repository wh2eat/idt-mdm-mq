package com.idata.mq.connection.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.idata.mq.base.exception.SendMessageException;
import com.idata.mq.base.message.DeviceOfflineMessage;
import com.idata.mq.base.message.DeviceOnlineMessage;
import com.idata.mq.connection.service.Connection2MdmMessageSendService;

public class Application {

    private static int getNum(String key, Map<String, Integer> map) {
        int num = 0;
        if (map.containsKey(key)) {
            num = map.get(key);
        }
        num = num + 1;
        map.put(key, num);
        return num;
    }

    public static void main(String[] args) throws SendMessageException {

        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-context.xml");

        final Connection2MdmMessageSendService messageSenderService = (Connection2MdmMessageSendService) context
                .getBean("connection2MdmMessageSendService");

        final RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();

        final ApplicationStatus status = new ApplicationStatus();
        status.setQuit(false);

        String commandModel = "manual";

        if (null != args && args.length > 0) {
            commandModel = args[0];
        }

        if ("manual".equals(commandModel)) {
            System.out.println("服务启动,手动测试模式");
        }
        else if ("auto".equals(commandModel)) {
            System.out.println("服务启动,自动测试模式");
        }
        else {
            System.out.println("服务启动失败,参数错误：" + commandModel);
            return;
        }

        Map<String, Integer> countMap = new HashMap<>();

        ThreadGroup threadGroup = new ThreadGroup("test-group");

        System.out.println("please send message.");

        // System.setProperty("Log4jContextSelector",
        // "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");

        boolean isQuit = false;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            String line = null;
            while (!isQuit) {
                line = reader.readLine();
                System.out.println("----------  " + line + "  --------");
                if (StringUtils.isNotBlank(line)) {
                    if ("quit".equals(line)) {
                        System.out.println("applicaton exist.");
                        isQuit = true;
                        // status.setQuit(true);
                        break;
                    }
                    if ("manual".equals(commandModel)) {
                        if (line.startsWith("send-device-online")) {

                            DeviceOnlineMessage onlineMessage = new DeviceOnlineMessage();

                            String[] params = line.split(" ");
                            if (params.length > 1) {
                                onlineMessage.setGuid(params[1]);
                            }
                            else {
                                onlineMessage.setGuid(generator.generate(32));
                            }

                            if (params.length > 2) {
                                onlineMessage.setIp(params[2]);
                            }
                            else {
                                onlineMessage.setIp("192.168.1.20");
                            }

                            if (params.length > 3) {
                                onlineMessage.setNetType(params[3]);
                            }
                            else {
                                onlineMessage.setNetType("wifi");
                            }
                            messageSenderService.sendDeviceOnline(onlineMessage);
                            System.out.println(">>>" + JSONObject.toJSONString(onlineMessage));
                        }
                        else if (line.startsWith("send-device-offline")) {
                            DeviceOfflineMessage offlineMessage = new DeviceOfflineMessage();

                            String[] params = line.split(" ");
                            if (params.length > 1) {
                                offlineMessage.setGuid(params[1]);
                            }
                            else {
                                offlineMessage.setGuid(generator.generate(32));
                            }

                            messageSenderService.sendDeviceOffline(offlineMessage);
                            System.out.println(">>>" + JSONObject.toJSONString(offlineMessage));
                        }
                        else if (line.startsWith("send-device-message-result")) {

                            String messageId = null;
                            String[] params = line.split(" ");
                            if (params.length > 1) {
                                messageId = params[1];
                            }
                            else {
                                messageId = generator.generate(32).toString();
                            }
                            Integer result = 0;
                            if (params.length > 2) {
                                result = Integer.parseInt(params[2].trim());
                            }
                            else {
                                result = RandomUtils.nextInt(0, 2);
                            }

                            messageSenderService.sendMessageResult(messageId, result);
                            System.out.println(">>>" + messageId + "," + result);
                        }
                        else if ("send-alive".equals(line)) {
                            messageSenderService.sendAlive();
                            System.out.println(">>>alive");
                        }
                    }
                    else {
                        if ("add-device-online-sender".equals(line)) {
                            String ln = "conn-device-online-sender";
                            String tname = ln + "-" + getNum(ln, countMap);
                            Thread thread = new Thread(threadGroup, new Runnable() {
                                @Override
                                public void run() {

                                    System.out.println(Thread.currentThread().getName() + ",run");

                                    try {

                                        while (true && !status.isQuit()) {
                                            DeviceOnlineMessage onlineMessage = new DeviceOnlineMessage();
                                            onlineMessage.setGuid(generator.generate(32));
                                            onlineMessage.setIp("192.168.1.20");
                                            onlineMessage.setNetType("wifi");
                                            messageSenderService.sendDeviceOnline(onlineMessage);
                                            Thread.currentThread();
                                            Thread.sleep(RandomUtils.nextInt(10, 100));
                                        }
                                    }
                                    catch (SendMessageException | InterruptedException e) {
                                        System.err.println(e.getMessage());
                                    }
                                    System.out.println(Thread.currentThread().getName() + ",quit");
                                }
                            }, tname);
                            thread.start();
                        }
                        else if ("add-device-offline-sender".equals(line)) {
                            String ln = "conn-device-offline-sender";
                            String tname = ln + "-" + getNum(ln, countMap);
                            Thread thread = new Thread(threadGroup, new Runnable() {
                                @Override
                                public void run() {

                                    System.out.println(Thread.currentThread().getName() + ",run");

                                    try {
                                        while (!status.isQuit()) {
                                            DeviceOfflineMessage offlineMessage = new DeviceOfflineMessage();
                                            offlineMessage.setGuid(generator.generate(32));
                                            messageSenderService.sendDeviceOffline(offlineMessage);
                                            Thread.currentThread();
                                            Thread.sleep(RandomUtils.nextInt(10, 100));
                                        }
                                    }
                                    catch (SendMessageException | InterruptedException e) {
                                        System.err.println(e.getMessage());
                                    }
                                    System.out.println(Thread.currentThread().getName() + ",quit");
                                }
                            }, tname);
                            thread.start();
                        }
                        else if ("add-device-message-result-sender".equals(line)) {
                            String ln = "conn-device-message-result-sender";
                            String tname = ln + "-" + getNum(ln, countMap);
                            Thread thread = new Thread(threadGroup, new Runnable() {
                                @Override
                                public void run() {

                                    System.out.println(Thread.currentThread().getName() + ",run");

                                    try {
                                        while (!status.isQuit()) {
                                            String messgeId = generator.generate(64);
                                            messageSenderService.sendMessageResult(messgeId, 1);
                                            Thread.currentThread();
                                            Thread.sleep(RandomUtils.nextInt(10, 100));
                                        }
                                    }
                                    catch (SendMessageException | InterruptedException e) {
                                        System.err.println(e.getMessage());
                                    }
                                    System.out.println(Thread.currentThread().getName() + ",quit");
                                }
                            }, tname);
                            thread.start();
                        }
                        else if ("add-alive-sender".equals(line)) {
                            String ln = "conn-alive-sender";
                            String tname = ln + "-" + getNum(ln, countMap);
                            Thread aliveThread = new Thread(threadGroup, new Runnable() {
                                @Override
                                public void run() {

                                    System.out.println(Thread.currentThread().getName() + ",run");

                                    try {
                                        while (true && !status.isQuit()) {
                                            messageSenderService.sendAlive();
                                            Thread.currentThread();
                                            Thread.sleep(RandomUtils.nextInt(1, 30) * 1000);
                                        }
                                    }
                                    catch (SendMessageException | InterruptedException e) {
                                        System.err.println(e.getMessage());
                                    }
                                    System.out.println(Thread.currentThread().getName() + ",quit");
                                }
                            }, tname);
                            aliveThread.start();
                        }
                        else if ("stop-sender".equals(line)) {
                            status.setQuit(true);
                        }
                        else if ("get-sender-status".equals(line)) {
                            System.out.println("activate:" + threadGroup.activeCount());
                        }
                    }
                }
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
