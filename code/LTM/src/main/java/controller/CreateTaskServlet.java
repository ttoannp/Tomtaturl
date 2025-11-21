package controller;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import dao.TaskDAO;
import model.Task;
import model.User;
import org.json.JSONObject;
import util.RabbitMQConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/create-task")
public class CreateTaskServlet extends HttpServlet {

    private final TaskDAO taskDAO = new TaskDAO();
    private static final String QUEUE_NAME = "task_queue";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String url = req.getParameter("url");
        long userId = currentUser.getId();

        if (url != null && !url.trim().isEmpty()) {

            // 1. Tạo Task object
            Task task = new Task();
            task.setUserId(userId);
            JSONObject payload = new JSONObject();
            payload.put("url", url);
            task.setPayloadJson(payload.toString());

            // 2. Lưu task xuống DB
            long newTaskId = taskDAO.createTask(task);

            if (newTaskId != -1) {
                // 3. Gửi taskId vào RabbitMQ để worker xử lý
                try (Connection connection = RabbitMQConnection.getConnection();
                     Channel channel = connection.createChannel()) {

                    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
                    String message = String.valueOf(newTaskId);

                    channel.basicPublish(
                            "",
                            QUEUE_NAME,
                            null,
                            message.getBytes(StandardCharsets.UTF_8)
                    );

                    System.out.println("[WebApp] Đã gửi task ID " + newTaskId + " vào RabbitMQ.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 4. Quay lại trang danh sách task
        resp.sendRedirect(req.getContextPath() + "/my-tasks");
    }
}
