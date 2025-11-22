package socket;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.net.Socket;

public class WebAppNotifier {

    private static final String HOST = "localhost"; // worker
    private static final int PORT = 9999;

    public void sendLoginEvent(long userId) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {

            JSONObject json = new JSONObject();
            json.put("type", "login");
            json.put("userId", userId);

            pw.println(json.toString());

            System.out.println("[WebApp] Sent login event userId=" + userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
