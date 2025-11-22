package worker;

import dao.UserDAO;
import dao.LoginHistoryDAO;
import model.User;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class LoginEventServer implements Runnable {

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(9001)) {
            System.out.println("[Worker-LOGIN] Listening login events on port 9001...");

            while (true) {
                Socket client = server.accept();
                new Thread(() -> handle(client)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handle(Socket socket) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String line = br.readLine();
            if (line == null) return;

            long userId = Long.parseLong(line.trim());   // Chỉ parse số, KHÔNG parse JSON

            UserDAO userDAO = new UserDAO();
            LoginHistoryDAO loginHistoryDAO = new LoginHistoryDAO();

            userDAO.updateLastLogin(userId);
            loginHistoryDAO.logLogin(userId);

            System.out.println("[Worker-LOGIN] Login OK user=" + userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
