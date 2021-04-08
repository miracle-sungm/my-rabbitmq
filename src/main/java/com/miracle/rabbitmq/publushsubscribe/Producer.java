package com.miracle.rabbitmq.publushsubscribe;

import com.miracle.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;

/**
 * @author : sungm
 * @date : 2021-03-29 16:37
 */
class Producer {

    /** 交换机名称 */
    static final String EXCHANGE_NAME = "fanout-exchange";
    /** 消息队列名称 */
    static final String[] QUEUE_NAMES = {"fanout-queue1", "fanout-queue2", "fanout-queue3"};
    /** 消息内容 */
    static final byte[] MESSAGE = "Hello, RabbitMQ, that is publish/subscribe model!".getBytes(StandardCharsets.UTF_8);

    public static void main(String[] args) {
        try (
                Connection connection = ConnectionUtils.newConnection();
                Channel channel = connection.createChannel();
        ) {
            //声明交换机并指定类型
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            //声明队列并绑定队列
            for (String queue : QUEUE_NAMES) {
                channel.queueDeclare(queue
                        , Boolean.FALSE
                        , Boolean.FALSE
                        , Boolean.FALSE
                        , null);
                //绑定一次之后就不需要再绑定了，为了保险起见，这里每次执行main方法都去绑定。
                //fanout模式不需要指定routingKey, 在这种模式下发布消息会把消息添加到绑定的每个队列
                channel.queueBind(queue, EXCHANGE_NAME, "");
            }
            //发送消息
            channel.basicPublish(EXCHANGE_NAME
                    , ""
                    , null
                    , MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
