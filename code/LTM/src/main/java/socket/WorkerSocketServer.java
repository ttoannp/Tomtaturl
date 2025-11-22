package socket;

import dao.TaskDAO;
import org.json.JSONObject;
import websocket.TaskSocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class WorkerSocketServer implements Runnable {

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(9999)) {
            System.out.println("[WebApp] Listening Worker on port 9999...");

            while (true) {
                Socket client = server.accept();
                new Thread(() -> handle(client)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handle(Socket client) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()))) {

            String line;
            while ((line = br.readLine()) != null) {

                JSONObject msg = new JSONObject(line);

                if (!msg.has("taskId") || !msg.has("userId") || !msg.has("status")) {
                    System.err.println("[WebApp] Worker gửi JSON không hợp lệ: " + msg);
                    continue;
                }

                long taskId = msg.getLong("taskId");
                long userId = msg.getLong("userId");
                String status = msg.getString("status");
                String error = msg.optString("error", null);

                TaskDAO dao = new TaskDAO();

                if ("DONE".equals(status)) {
                    dao.updateTaskStatusToDone(taskId);
                } else {
                    dao.updateTaskStatusToFailed(taskId, error);
                }

                System.out.println("[WebApp] Task " + taskId + " cập nhật " + status + " — gửi cho user " + userId);

                // Gửi WebSocket UI real-time
                TaskSocket.sendToUser(userId, msg.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
