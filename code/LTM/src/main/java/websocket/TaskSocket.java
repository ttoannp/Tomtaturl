package websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(
        value = "/task-ws",
        configurator = websocket.GetHttpSessionConfigurator.class
)
public class TaskSocket {

    private static final Map<Long, Session> userSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        User user = (User) config.getUserProperties().get("user");

        if (user != null) {
            userSessions.put(user.getId(), session);
            System.out.println("[WebSocket] User " + user.getId() + " connected.");
        }
    }

    @OnClose
    public void onClose(Session session) {
        // Tìm và xóa session khỏi map
        userSessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
        System.out.println("[WebSocket] User disconnected. Remaining sessions: " + userSessions.size());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public static void sendToUser(long userId, String message) {
        try {
            Session s = userSessions.get(userId);
            if (s != null && s.isOpen()) {
                s.getBasicRemote().sendText(message);
                System.out.println("[WebSocket] Sent to user " + userId + ": " + message);
            } else {
                if (s == null) {
                    System.out.println("[WebSocket] WARNING: Không tìm thấy session cho userId " + userId + ". Active sessions: " + userSessions.keySet());
                } else if (!s.isOpen()) {
                    System.out.println("[WebSocket] WARNING: Session cho userId " + userId + " đã đóng. Đang xóa khỏi map...");
                    userSessions.remove(userId);
                }
            }
        } catch (Exception e) {
            System.err.println("[WebSocket] Lỗi khi gửi message cho user " + userId + ": " + e.getMessage());
            e.printStackTrace();
            // Xóa session nếu có lỗi
            userSessions.remove(userId);
        }
    }
}
