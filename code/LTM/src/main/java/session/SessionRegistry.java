// src/main/java/session/SessionRegistry.java
package session;

import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Quản lý phiên đăng nhập của từng user.
 * Mỗi userId chỉ được gắn với 1 HttpSession.
 */
public class SessionRegistry {

    // userId -> HttpSession
    private static final Map<Long, HttpSession> USER_SESSIONS = new ConcurrentHashMap<>();

    /**
     * Đăng ký một session mới cho user.
     * Nếu user đã có session khác, session cũ sẽ bị invalidate (đá ra).
     */
    public static synchronized void registerSession(Long userId, HttpSession newSession) {
        if (userId == null || newSession == null) return;

        HttpSession old = USER_SESSIONS.get(userId);

        System.out.println("[SessionRegistry] registerSession userId=" + userId
                + ", newSessionId=" + newSession.getId()
                + ", oldSessionId=" + (old == null ? "null" : old.getId()));

        if (old != null && old != newSession) {
            try {
                System.out.println("[SessionRegistry] Đang invalidate session CŨ ("
                        + old.getId() + ") của userId = " + userId);
                old.invalidate();
            } catch (IllegalStateException ignore) {
                System.out.println("[SessionRegistry] Session cũ đã invalid sẵn: " + old.getId());
            }
        } else if (old == newSession) {
            System.out.println("[SessionRegistry] old == newSession (cùng một HttpSession) -> không invalidate.");
        } else {
            System.out.println("[SessionRegistry] Chưa có session cũ cho userId=" + userId + ".");
        }

        USER_SESSIONS.put(userId, newSession);
        System.out.println("[SessionRegistry] Gắn session MỚI " + newSession.getId()
                + " cho userId = " + userId);
    }

    /**
     * Xoá một session khỏi registry (khi logout hoặc session timeout).
     */
    public static synchronized void removeSession(HttpSession session) {
        if (session == null) return;

        USER_SESSIONS.entrySet().removeIf(e -> e.getValue().equals(session));
        System.out.println("[SessionRegistry] Đã xoá session " + session.getId() + " khỏi registry");
    }
}
