package com.miracle.rabbitmq.topics;

import com.miracle.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;

/**
 * @author : sungm
 * @date : 2021-03-29 20:15
 */
class Producer {

    /** 交换机名称 */
    static final String EXCHANGE_NAME = "topic_exchange";
    /** 队列名称 */
    static final String[] QUEUE_NAMES = {"topic_queue1", "topic_queue2", "topic_queue3"};
    /**
     * 路由
     *
     * 说明：'#' -> 表示可以匹配0个、1个或多个； '*' -> 只能匹配一个。
     */
    static final String[] ROUTING_KEYS = {"order.#", "order.*", "user.*"};
    /** 消息内容 */
    static final byte[] MESSAGE = "Hello, RabbitMQ, that is topic model!".getBytes(StandardCharsets.UTF_8);

    public static void main(String[] args) {
        try (
                final Connection connection = ConnectionUtils.newConnection();
                final Channel channel = connection.createChannel();
        ) {
            //删除交换机
            channel.exchangeDelete(EXCHANGE_NAME);
            //声明交换机，类型为：topic
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            for (int index = 0; index < QUEUE_NAMES.length; index++) {
                //删除队列
                channel.queueDelete(QUEUE_NAMES[index]);
                //声明队列
                channel.queueDeclare(QUEUE_NAMES[index], Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
                //绑定队列
                channel.queueBind(QUEUE_NAMES[index], EXCHANGE_NAME, ROUTING_KEYS[index]);
            }
            //发布消息
            channel.basicPublish(EXCHANGE_NAME, "order.huawei", null, MESSAGE);
            channel.basicPublish(EXCHANGE_NAME, "order.huawei.phone", null, MESSAGE);
            channel.basicPublish(EXCHANGE_NAME, "order.apple", null, MESSAGE);
            channel.basicPublish(EXCHANGE_NAME, "order.apple.mac", null, MESSAGE);
            channel.basicPublish(EXCHANGE_NAME, "user.list", null, MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
