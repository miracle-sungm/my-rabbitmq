package com.miracle.rabbitmq.routing;

import com.miracle.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.Charset;

/**
 * @author : sungm
 * @date : 2021-03-29 17:39
 */
class Consumer {

    public static void main(String[] args) {
        try (
                final Connection connection = ConnectionUtils.newConnection();
                final Channel channel = connection.createChannel()) {
            for (Producer.BindQueueEnum bindQueueEnum : Producer.BindQueueEnum.values()) {
                //消费消息，指定队列名
                channel.basicConsume(bindQueueEnum.getQueueName()
                        , Boolean.TRUE
                        , (consumerTag, message) -> System.out.println(new String(message.getBody(), Charset.defaultCharset()))
                        , consumerTag -> {});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
