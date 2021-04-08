package com.miracle.rabbitmq.workqueues.fair;

import com.miracle.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;

/**
 * @author : sungm
 * @date : 2021-03-29 21:21
 */
public class Producer {

    static final String EXCHANGE_NAME = "fair_work_queue_exchange";
    static final String QUEUE_NAME = "fair_work_queue";
    static final String ROUTING_KEY = "fair_work_routing_key";
    static final int TOTAL = 50;

    public static void main(String[] args) {
        try (
                final Connection connection = ConnectionUtils.newConnection();
                final Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(QUEUE_NAME, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
            for (int number = 0; number < TOTAL; number++) {
                String message = "Hello, RabbitMQ, that's message number " + number;
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
