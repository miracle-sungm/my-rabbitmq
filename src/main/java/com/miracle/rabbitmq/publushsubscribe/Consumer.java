package com.miracle.rabbitmq.publushsubscribe;

import com.miracle.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import static com.miracle.rabbitmq.publushsubscribe.Producer.QUEUE_NAMES;

/**
 * @author : sungm
 * @date : 2021-03-29 16:51
 */
class Consumer {

    public static void main(String[] args) {
        try (
                Connection connection = ConnectionUtils.newConnection();
                final Channel channel = connection.createChannel()
        ) {
            for (String queue : QUEUE_NAMES) {
                consumer(channel, queue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void consumer(final Channel channel, final String queue) throws Exception {
        channel.basicConsume(queue
                , Boolean.TRUE
                , (consumerTag, message) -> System.out.println(queue + new String(message.getBody()))
                , consumerTag -> {});
    }

}
