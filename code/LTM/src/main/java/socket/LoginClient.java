package socket;

import java.io.PrintWriter;
import java.net.Socket;

public class LoginClient {

    private static final String HOST = "localhost";
    private static final int PORT = 9001;

    public static void sendLogin(Long userId) throws Exception {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {

            pw.println(userId.toString());
            System.out.println("[WebApp] Đã gửi login event userId=" + userId + " sang Worker");
        }
    }
}
