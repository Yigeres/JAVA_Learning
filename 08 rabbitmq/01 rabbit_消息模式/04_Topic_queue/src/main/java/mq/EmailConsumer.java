package mq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: create by wangmh
 * @name: EmailConsumer.java
 * @description:
 * @date:2019/12/15
 **/
public class EmailConsumer {
    /***
     * 定义邮件队列
     */
    private static final String QUEUE_NAME = "topic_email";
    /**
     * 定义交换机的名称
     */
    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("邮件消费者...");
        // 创建我们的连接
        Connection connection = RabitMQConnection.getConnection();
        // 创建我们通道
        final Channel channel = connection.createChannel();
        // 关联队列消费者关联队列
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "wmh.*");//参数3为队列所绑定的路由键
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("邮件消费者获取消息:" + msg);
            }
        };
        // 开始监听消息 自动签收
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);

    }

}
