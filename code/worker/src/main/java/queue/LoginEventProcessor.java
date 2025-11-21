// worker/src/main/java/queue/LoginEventProcessor.java
package queue;

import dao.LoginHistoryDAO;
import dao.UserDAO;

/**
 * Xử lý sự kiện đăng nhập thành công gửi từ web qua RabbitMQ.
 * Message = userId dạng chuỗi, ví dụ "2".
 */
public class LoginEventProcessor {

    private final UserDAO userDAO;
    private final LoginHistoryDAO loginHistoryDAO;

    public LoginEventProcessor() {
        this.userDAO = new UserDAO();
        this.loginHistoryDAO = new LoginHistoryDAO();
    }

    /**
     * Xử lý một message login.
     * @param message userId dạng chuỗi.
     */
    public void process(String message) {
        Long userId;
        try {
            userId = Long.parseLong(message.trim());
        } catch (NumberFormatException e) {
            System.err.println("[LoginEventProcessor] Message không phải userId hợp lệ: " + message);
            return;
        }

        try {
            System.out.println("[LoginEventProcessor] Bắt đầu xử lý login cho userId=" + userId);

            // 1) Cập nhật last_login
            userDAO.updateLastLogin(userId);
            System.out.println("[LoginEventProcessor] Cập nhật last_login OK cho userId=" + userId);

            // 2) Ghi login_history
            loginHistoryDAO.logLogin(userId);
            System.out.println("[LoginEventProcessor] Ghi login_history OK cho userId=" + userId);

        } catch (Exception e) {
            System.err.println("[LoginEventProcessor] Lỗi khi xử lý login cho userId=" + userId);
            e.printStackTrace();
        }
    }
}
