package com.miracle.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : sungm
 * @date : 2021-03-29 14:57
 */
public class ConnectionUtils {

    private ConnectionUtils() {
    }

    private static final ConnectionFactory CONNECTION_FACTORY;

    static {
        CONNECTION_FACTORY = new ConnectionFactory();
        try {
            CONNECTION_FACTORY.load("classpath:/rabbitmq.properties", "rabbitmq.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection newConnection() throws IOException, TimeoutException {
        return CONNECTION_FACTORY.newConnection();
    }

}
