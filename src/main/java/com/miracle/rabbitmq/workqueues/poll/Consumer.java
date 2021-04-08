package com.miracle.rabbitmq.workqueues.poll;

import com.miracle.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;

import static com.miracle.rabbitmq.workqueues.poll.Producer.QUEUE_NAME;

/**
 * @author : sungm
 * @date : 2021-03-29 21:21
 */
class Consumer {

    public static void main(String[] args) {
        try (
                //创建连接
                final Connection connection = ConnectionUtils.newConnection();
                //创建通道
                final Channel channel = connection.createChannel()
        ) {
            //消费消息：指定队列名，是否应答等
            //ack -> true: 应答消息，会消费掉一条消息; false -> 不应答消息，只是获取消息内容，不消费消息。
            //设置Qos
            channel.basicQos(1);
            channel.basicConsume(QUEUE_NAME, Boolean.FALSE
                    //消息回调
                    , (consumerTag, message) -> {
                        System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //手动应答
                        channel.basicAck(message.getEnvelope().getDeliveryTag(), Boolean.FALSE);
                    }
                    //取消回调
                    , consumerTag -> {});
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class OtherConsumer {
    public static void main(String[] args) {
        try (
                //创建连接
                final Connection connection = ConnectionUtils.newConnection();
                //创建通道
                final Channel channel = connection.createChannel()
        ) {
            //消费消息：指定队列名，是否应答等
            //ack -> true: 应答消息，会消费掉一条消息; false -> 不应答消息，只是获取消息内容，不消费消息。
            channel.basicQos(1);
            channel.basicConsume(QUEUE_NAME, Boolean.FALSE
                    //消息回调
                    , (consumerTag, message) -> {
                        System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        channel.basicAck(message.getEnvelope().getDeliveryTag(), Boolean.FALSE);
                    }
                    //取消回调
                    , channel::basicCancel);
            System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
