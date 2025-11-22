	package worker;
	
	import org.json.JSONObject;
	
	import java.io.PrintWriter;
	import java.net.Socket;
	
	/**
	 * Kết nối tới WebApp (socket server) để báo "DONE"/"FAILED"
	 */
	public class WorkerNotifier {
	
	    // Địa chỉ server WebApp
	    private static final String HOST = "localhost";
	    private static final int PORT = 9999;
	
	    public void sendToWebApp(JSONObject msg) {
	        try (Socket socket = new Socket(HOST, PORT);
	             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {
	
	            pw.println(msg.toString());
	            System.out.println("[Worker] Đã gửi status '" + msg.optString("status") +
	                    "' cho task " + msg.optLong("taskId") + " về WebApp");
	
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.err.println("[Worker] Lỗi khi gửi status về WebApp: " + e.getMessage());
	        }
	    }
	}
