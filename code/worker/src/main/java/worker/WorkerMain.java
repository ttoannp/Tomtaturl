package worker;

public class WorkerMain {

    public static void main(String[] args) {
        System.out.println("[Worker] WorkerMain starting...");

        // Thread 1: loop xử lý task (cào URL, tạo PDF, lưu result,...)
        Thread taskThread = new Thread(() -> {
            TaskWorker worker = new TaskWorker();
            worker.startLoop();
        });
        taskThread.setDaemon(false);
        taskThread.start();

        // Thread 2: lắng nghe login event từ WebApp
        Thread loginThread = new Thread(new LoginEventServer());
        loginThread.setDaemon(false);
        loginThread.start();

        System.out.println("[Worker] TaskWorker & LoginEventServer đã khởi động.");

        try {
            // giữ cho main không thoát
            taskThread.join();
            loginThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
