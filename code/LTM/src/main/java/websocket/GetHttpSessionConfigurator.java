package websocket;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.*;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig config,
                                HandshakeRequest request,
                                HandshakeResponse response) {

        HttpSession httpSession = (HttpSession) request.getHttpSession();

        if (httpSession != null) {
            config.getUserProperties().put("user",
                    httpSession.getAttribute("currentUser"));
        }
    }
}
