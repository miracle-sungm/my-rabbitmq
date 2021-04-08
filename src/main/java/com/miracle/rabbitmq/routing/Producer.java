package com.miracle.rabbitmq.routing;

import com.miracle.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;

/**
 * @author : sungm
 * @date : 2021-03-29 17:39
 */
class Producer {

    /** 交换机名称 */
    static final String EXCHANGE_NAME = "direct_exchange";
    static final byte[] MESSAGE = "Hello, RabbitMQ, that is routing model!".getBytes(StandardCharsets.UTF_8);

    enum BindQueueEnum {

        /**
         * 队列名及路由绑定信息
         */
        QUEUE1("direct_queue1", "queue1"),
        QUEUE2("direct_queue2", "queue2"),
        QUEUE3("direct_queue3", "queue3");

        private final String queueName;
        private final String routingKey;

        BindQueueEnum(String queueName, String routingKey) {
            this.queueName = queueName;
            this.routingKey = routingKey;
        }

        public String getQueueName() {
            return queueName;
        }

        public String getRoutingKey() {
            return routingKey;
        }
    }

    public static void main(String[] args) {
        try (
                final Connection connection = ConnectionUtils.newConnection();
                final Channel channel = connection.createChannel()) {
            //创建交换机，这里指定direct类型（路由模式下设置direct很重要）
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            for (BindQueueEnum bindQueueEnum : BindQueueEnum.values()) {
                //声明队列
                channel.queueDeclare(bindQueueEnum.getQueueName(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
                //绑定队列
                channel.queueBind(bindQueueEnum.getQueueName(), EXCHANGE_NAME, bindQueueEnum.getRoutingKey());
            }
            //发送消息，指定路由发送
            channel.basicPublish(EXCHANGE_NAME, BindQueueEnum.QUEUE1.getRoutingKey(), null, MESSAGE);
            channel.basicPublish(EXCHANGE_NAME, BindQueueEnum.QUEUE2.getRoutingKey(), null, MESSAGE);
            channel.basicPublish(EXCHANGE_NAME, BindQueueEnum.QUEUE3.getRoutingKey(), null, MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
