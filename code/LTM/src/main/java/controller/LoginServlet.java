// src/main/java/controller/LoginServlet.java
package controller;

import dao.UserDAO;
import model.User;
import session.SessionRegistry;
import util.RabbitMQConnection;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Lấy thông tin user từ DB
        User user = userDAO.getUserByUsername(username);

        // Kiểm tra mật khẩu (plaintext theo schema hiện tại)
        if (user != null && user.getPassword() != null
                && user.getPassword().equals(password)) {

            // Đăng nhập thành công → tạo / lấy session
            HttpSession session = req.getSession(true);
            session.setAttribute("currentUser", user);

            // 🔒 Chống đăng nhập nhiều nơi:
            // userId chỉ được gắn với 1 HttpSession. Session cũ sẽ bị invalidate.
            SessionRegistry.registerSession(user.getId(), session);

            // 📨 Gửi sự kiện đăng nhập cho worker qua RabbitMQ
            publishLoginEvent(user);

            // Điều hướng về trang my-tasks
            resp.sendRedirect(req.getContextPath() + "/my-tasks");

        } else {
            // Đăng nhập thất bại
            req.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }

    /**
     * Gửi userId lên queue "login_queue" để worker xử lý
     * (update last_login, ghi login_history, v.v.).
     */
    private void publishLoginEvent(User user) {
        if (user == null) return;

        // 👇 TÊN QUEUE PHẢI GIỐNG BÊN WORKER
        String queueName = "login_queue";
        String body = String.valueOf(user.getId()); // Worker parseLong(message)

        try {
            var connection = RabbitMQConnection.getConnection();
            Channel channel = connection.createChannel();
            try {
                channel.queueDeclare(queueName, true, false, false, null);

                channel.basicPublish(
                        "",
                        queueName,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        body.getBytes(StandardCharsets.UTF_8)
                );

                System.out.println("[LoginServlet] Đã gửi LOGIN event cho userId = " + user.getId());
            } finally {
                channel.close(); // Đóng channel, giữ connection
            }
        } catch (Exception e) {
            System.err.println("[LoginServlet] Lỗi khi gửi LOGIN event cho userId = " + user.getId());
            e.printStackTrace();
        }
    }
}
