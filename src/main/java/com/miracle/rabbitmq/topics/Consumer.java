package com.miracle.rabbitmq.topics;

import com.miracle.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;

import static com.miracle.rabbitmq.topics.Producer.QUEUE_NAMES;

/**
 * @author : sungm
 * @date : 2021-03-29 20:16
 */
class Consumer {

    public static void main(String[] args) {
        try (
                //创建连接
                final Connection connection = ConnectionUtils.newConnection();
                //创建通道
                final Channel channel = connection.createChannel()
        ) {
            for (String queueName : QUEUE_NAMES) {
                //消费消息：指定队列名，是否应答等
                //ack -> true: 应答消息，会消费掉一条消息; false -> 不应答消息，只是获取消息内容，不消费消息。
                channel.basicConsume(queueName, Boolean.TRUE
                        //消息回调
                        , (consumerTag, message) -> System.out.println(new String(message.getBody(), StandardCharsets.UTF_8))
                        //取消回调
                        , consumerTag -> {});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
