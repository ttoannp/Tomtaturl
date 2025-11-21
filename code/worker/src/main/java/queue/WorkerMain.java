package queue;

import com.rabbitmq.client.*;
import util.RabbitMQConnection;

import java.nio.charset.StandardCharsets;

public class WorkerMain {

    private static final String QUEUE_NAME = "task_queue";
    private static final String LOGIN_QUEUE = "login_queue";

    public static void main(String[] args) throws Exception {

        TaskProcessor processor = new TaskProcessor();

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        // Khai báo queue (phải giống webapp)
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        System.out.println("[Worker] Đang lắng nghe queue '" + QUEUE_NAME + "' ...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[Worker] Nhận message từ RabbitMQ: " + message);

            try {
                long taskId = Long.parseLong(message.trim());
                processor.processTask(taskId);
            } catch (NumberFormatException e) {
                System.err.println("[Worker] Không parse được taskId từ message: " + message);
            }
        };

        CancelCallback cancelCallback = consumerTag ->
                System.out.println("[Worker] Consumer bị hủy: " + consumerTag);

        boolean autoAck = true; // cho đồ án: xử lý đơn giản
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
        
        LoginEventProcessor loginProcessor = new LoginEventProcessor();
        
        Channel loginChannel = connection.createChannel();
        loginChannel.queueDeclare(LOGIN_QUEUE, true, false, false, null);
        
        System.out.println("[Worker-LOGIN] Đang lắng nghe queue '" + LOGIN_QUEUE + "' ...");
        
        DeliverCallback loginCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[Worker-LOGIN] Nhận login event: " + msg);
            // message = userId dưới dạng chuỗi
            loginProcessor.process(msg);
            // autoAck = true nên không cần ack tay
        };
        
        CancelCallback loginCancelCallback = consumerTag ->
        System.out.println("[Worker-LOGIN] Consumer bị hủy: " + consumerTag);
        
        boolean autoAckLogin = true; // cho đơn giản giống task_queue
        loginChannel.basicConsume(LOGIN_QUEUE, autoAckLogin, loginCallback, loginCancelCallback);

        System.out.println("[Worker] Worker đang chạy. Nhấn Ctrl+C để dừng.");
        Thread.currentThread().join();  // giữ cho main-thread không thoát
    }
}
