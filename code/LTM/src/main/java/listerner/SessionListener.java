// src/main/java/listener/SessionListener.java
package listerner;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import session.SessionRegistry;

/**
 * Lắng nghe vòng đời HttpSession để dọn dẹp trong SessionRegistry.
 */
@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        SessionRegistry.removeSession(se.getSession());
    }
}
