package listerner;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import socket.WorkerSocketServer;

@WebListener
public class AppStartup implements ServletContextListener {

    private Thread workerSocketThread;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("[WebApp] AppStartup: Khởi động WorkerSocketServer...");

        try {
            workerSocketThread = new Thread(new WorkerSocketServer());
            workerSocketThread.setDaemon(true); // để Tomcat shutdown không bị kẹt
            workerSocketThread.start();

            System.out.println("[WebApp] WorkerSocketServer đã được start trên port 9999");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[WebApp] Lỗi khi start WorkerSocketServer: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[WebApp] AppStartup: Dừng ứng dụng...");

        try {
            if (workerSocketThread != null && workerSocketThread.isAlive()) {
                workerSocketThread.interrupt();
                System.out.println("[WebApp] WorkerSocketServer đã được dừng.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
