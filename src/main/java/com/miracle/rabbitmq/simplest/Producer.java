package com.miracle.rabbitmq.simplest;

import com.miracle.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.nio.charset.StandardCharsets;

/**
 * @author : sungm
 * @date : 2021-03-29 14:14
 */
class Producer {

    /** 队列名称 */
    static final String QUEUE_NAME = "demo-simple-queue";
    /** 消息内容 */
    static final byte[] MESSAGE = "Hello, RabbitMQ, that is simplest model!".getBytes(StandardCharsets.UTF_8);

    public static void main(String[] args)  {
        try (
                //Step1: 创建连接
                Connection connection = ConnectionUtils.newConnection();
                //Step2: 通过连接获取通道Channel
                Channel channel = connection.createChannel()
        ) {
            //Step3: 声明队列
            //durable: 是否耐用（耐用队列在服务重启后仍然有效）
            //exclusive: 是否独占队列（独占队列只对当前连接有效）
            //autoDelete: 是否自动删除（服务器将在不再使用它时将其删除）
            //arguments: 其他参数配置
            channel.queueDeclare(QUEUE_NAME
                    , Boolean.FALSE
                    , Boolean.FALSE
                    , Boolean.FALSE
                    , null);
            //Step4: 发送消息给队列Queue
            //没有声明交换机：这里使用的是默认的交换机，默认的交换机是direct类型的
            channel.basicPublish(""
                    , QUEUE_NAME
                    , null
                    , MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
