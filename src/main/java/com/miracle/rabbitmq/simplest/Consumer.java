package com.miracle.rabbitmq.simplest;

import com.miracle.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;

import static com.miracle.rabbitmq.simplest.Producer.QUEUE_NAME;

/**
 * @author : sungm
 * @date : 2021-03-29 14:14
 */
class Consumer {


    public static void main(String[] args) {
        try (
                //创建连接
                Connection connection = ConnectionUtils.newConnection();
                //创建通道
                Channel channel = connection.createChannel()
        ) {
            //消费消息：指定队列名，是否应答等
            //autoAck -> true: 自动应答消息，会消费掉一条消息; false -> 不自动应答消息，只是获取消息内容，不消费消息。
            channel.basicConsume(QUEUE_NAME, Boolean.TRUE
                    //消息回调
                    , (consumerTag, message) -> System.out.println(new String(message.getBody(), StandardCharsets.UTF_8))
                    //取消回调
                    , consumerTag -> {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
