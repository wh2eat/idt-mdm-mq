
package com.idata.mq.mdm.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.idata.mq.base.exception.SendMessageException;
import com.idata.mq.base.message.CommandMessage;
import com.idata.mq.base.message.DeviceMessage;
import com.idata.mq.mdm.service.Mdm2ConnectionMessageSendServce;

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

        final Mdm2ConnectionMessageSendServce connectionMessageSenderServce = (Mdm2ConnectionMessageSendServce) context
                .getBean("mdm2ConnectionMessageSendServce");

        String model = "manual";

        if (null != args && args.length > 0) {
            model = args[0];
        }

        if ("manual".equals(model)) {
            System.out.println("服务启动,手动模式");
        }
        else if ("auto".equals(model)) {
            System.out.println("服务启动,自动模式");
        }
        else {
            System.out.println("服务启动失败,原因：未知的模式" + model);
            return;
        }

        // System.setProperty("Log4jContextSelector",
        // "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");

        final ApplicationStatus status = new ApplicationStatus();

        final RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();

        boolean isQuit = false;
        Map<String, Integer> countMap = new HashMap<>();

        ThreadGroup group = new ThreadGroup("mdm-server-test-sender-group");

        List<Thread> threads = new ArrayList<>();

        System.out.println("please send message.");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            String line = null;
            while (!isQuit) {
                line = reader.readLine();
                if (StringUtils.isNotBlank(line)) {
                    if ("quit".equals(line)) {
                        System.out.println("applicaton exist.");
                        isQuit = true;
                        break;
                    }

                    if ("manual".equals(model)) {
                        if ("send-device-message".equals(line)) {
                            DeviceMessage deviceMessage = new DeviceMessage();
                            deviceMessage.setGuid(generator.generate(32));
                            deviceMessage.setContent(generator.generate(RandomUtils.nextInt(10000, 50000)));
                            connectionMessageSenderServce.sendDeviceMessage(deviceMessage);
                            System.out.println(">>>" + JSONObject.toJSONString(deviceMessage));
                        }
                        else if ("get-connection-status".equals(line)) {
                            CommandMessage commandMessage = new CommandMessage();
                            commandMessage.setCommand("get-connection-server-status");
                            connectionMessageSenderServce.sendCommandMessage(commandMessage);
                            System.out.println(">>>" + JSONObject.toJSONString(commandMessage));
                        }
                        else if ("get-device-status".equals(line)) {
                            CommandMessage commandMessage = new CommandMessage();
                            commandMessage.setCommand("get-device-status");
                            commandMessage.setParameters(new String[] { "guid" });
                            connectionMessageSenderServce.sendCommandMessage(commandMessage);
                            System.out.println(">>>" + JSONObject.toJSONString(commandMessage));
                        }
                        else if ("send-alive".equals(line)) {
                            connectionMessageSenderServce.sendAlive();
                            System.out.println(">>>send alive");
                        }
                        else if ("help".equals(line)) {
                            System.out.println(
                                    "send-device-message:发送设备消息。\nget-connection-status：获取连接服务器状态。\nget-device-status：获取设备状态消息。\nsend-alive：发送服务器状态消息。\nquit：退出程序。");
                        }
                        else {
                            System.out.println("错误的命令");
                        }
                    }
                    else {

                        if ("add-device-message-sender".equals(line)) {

                            String ln = "device-message-sender";
                            String tname = ln + "-" + getNum(ln, countMap);

                            Thread thread = new Thread(group, new Runnable() {

                                @Override
                                public void run() {
                                    System.out.println(Thread.currentThread().getName() + ",run");
                                    try {
                                        while (!status.isQuit()) {
                                            DeviceMessage deviceMessage = new DeviceMessage();
                                            deviceMessage.setGuid(generator.generate(8) + "-" + generator.generate(8)
                                                    + "-" + generator.generate(8) + "-" + generator.generate(8));
                                            deviceMessage
                                                    .setContent(generator.generate(RandomUtils.nextInt(10000, 50000)));
                                            connectionMessageSenderServce.sendDeviceMessage(deviceMessage);

                                            Thread.currentThread();
                                            Thread.sleep(RandomUtils.nextLong(10, 100));
                                        }
                                    }
                                    catch (Exception e) {
                                        System.err.println(e.getMessage());
                                    }

                                    System.out.println(Thread.currentThread().getName() + ",quit");

                                }
                            }, tname);
                            threads.add(thread);
                            thread.start();
                        }
                        else if (line.startsWith("remove")) {
                            String[] params = line.split(" ");
                            String name = null;
                            if (params.length > 1) {
                                name = params[1];
                            }
                            int num = 1;
                            if (params.length > 2) {
                                try {
                                    num = Integer.parseInt(params[2].trim());
                                }
                                catch (Exception e) {
                                }
                            }

                            if (null != name) {
                                Iterator<Thread> iterator = threads.iterator();
                                int rmCount = 0;
                                while (iterator.hasNext() && rmCount < num) {
                                    Thread thread = iterator.next();
                                    if (thread.getName().startsWith(name)) {
                                        threads.remove(thread);
                                        thread.interrupt();
                                        rmCount++;
                                        System.out.println("remove thread:" + thread.getName());
                                    }
                                }
                            }
                            else {
                                System.err.println("error:name is null");
                            }
                        }
                        else if ("add-command-sender".equals(line)) {
                            String ln = "command-sender";
                            String tname = ln + "-" + getNum(ln, countMap);
                            Thread thread = new Thread(group, new Runnable() {

                                @Override
                                public void run() {
                                    System.out.println(Thread.currentThread().getName() + ",run");
                                    try {
                                        while (!status.isQuit()) {

                                            CommandMessage commandMessage = new CommandMessage();
                                            commandMessage.setCommand("get-connection-server-status");
                                            connectionMessageSenderServce.sendCommandMessage(commandMessage);

                                            Thread.currentThread();
                                            Thread.sleep(RandomUtils.nextLong(10, 100));
                                        }
                                    }
                                    catch (Exception e) {
                                        System.err.println(e.getMessage());
                                    }

                                    System.out.println(Thread.currentThread().getName() + ",quit");

                                }
                            }, tname);
                            threads.add(thread);
                            thread.start();
                        }
                        else if ("add-alive-sender".equals(line)) {

                            String ln = "alive-sender";
                            String tname = ln + "-" + getNum(ln, countMap);

                            Thread aliveThread = new Thread(group, new Runnable() {

                                @Override
                                public void run() {

                                    System.out.println(Thread.currentThread().getName() + ",run");

                                    try {
                                        while (!status.isQuit()) {
                                            connectionMessageSenderServce.sendAlive();
                                            Thread.currentThread();
                                            Thread.sleep(RandomUtils.nextInt(1, 30) * 1000);
                                        }
                                    }
                                    catch (SendMessageException e) {
                                        System.err.println(e.getMessage());
                                    }
                                    catch (InterruptedException e) {
                                        System.err.println(e.getMessage());
                                    }
                                    System.out.println(Thread.currentThread().getName() + ",quit");
                                }
                            }, tname);
                            threads.add(aliveThread);
                            aliveThread.start();
                        }
                        else if ("stop-sender".equals(line)) {
                            status.setQuit(true);
                        }
                        else if ("get-sender-status".equals(line)) {
                            System.out.println("activate:" + group.activeCount());
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
