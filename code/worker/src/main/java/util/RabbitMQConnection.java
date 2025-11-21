package util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConnection {

    private static final String HOST = "localhost"; // hoặc IP của RabbitMQ server
    private static final int PORT = 5672;
    private static final String USER = "guest";
    private static final String PASS = "guest";

    public static Connection getConnection() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername(USER);
        factory.setPassword(PASS);
        return factory.newConnection();
    }
}
