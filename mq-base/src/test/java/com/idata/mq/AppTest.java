package com.idata.mq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class AppTest {

    public static void main(String[] args) throws IOException, TimeoutException {
        /**
         * 配置amqp broker 连接信息
         */
        ConnectionFactory facotry = new ConnectionFactory();
        facotry.setUsername("bussiness_server");
        facotry.setPassword("123456");
        facotry.setVirtualHost("/mdm/server/bussiness-connecton");
        facotry.setHost("192.168.1.22");

        Connection conn = facotry.newConnection(); // 获取一个链接
        // 通过Channel进行通信
        Channel channel = conn.createChannel();

        // channel.exchangeDeclare(Send.EXCHANGE_NAME, "direct", true); //如果消费者已创建，这里可不声明
        channel.confirmSelect(); // Enables publisher acknowledgements on this channel
        channel.addConfirmListener(new ConfirmListener() {

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("[handleNack] :" + deliveryTag + "," + multiple);

            }

            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("[handleAck] :" + deliveryTag + "," + multiple);
            }
        });

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
            String line = null;
            while (true) {
                line = reader.readLine();
                if (null != line) {
                    channel.basicPublish("direct-exchange", "mdm-service-status",
                            MessageProperties.PERSISTENT_TEXT_PLAIN, "alive".getBytes());
                }
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
