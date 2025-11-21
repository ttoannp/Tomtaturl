package queue;

import bo.PdfGeneratorService;
import bo.ScrapeService;
import dao.ResultDAO;
import dao.TaskDAO;
import model.Result;
import model.Task;
import org.json.JSONObject;

public class TaskProcessor {

    private final TaskDAO taskDAO;
    private final ResultDAO resultDAO;
    private final ScrapeService scrapeService;
    private final PdfGeneratorService pdfService;

    public TaskProcessor() {
        this.taskDAO = new TaskDAO();
        this.resultDAO = new ResultDAO();
        this.scrapeService = new ScrapeService();
        this.pdfService = new PdfGeneratorService();
    }

    /**
     * Xử lý 1 task theo taskId.
     * Code này lấy gần như toàn bộ logic trong Worker.run() cũ của bạn.
     */
    public void processTask(long taskId) {
        System.out.println("[Worker] Bắt đầu xử lý task " + taskId);

        try {
            // SET PROCESSING
            try {
                taskDAO.updateTaskStatusToProcessing(taskId);
            } catch (Exception e) {
                System.err.println("Không thể set PROCESSING cho task " + taskId + ": " + e.getMessage());
            }

            // Load task
            Task task = taskDAO.getTaskById(taskId);
            if (task == null) {
                System.err.println("Task " + taskId + " không tồn tại. Bỏ qua...");
                return;
            }

            long userId = task.getUserId(); // vẫn giữ lại nếu sau này bạn muốn dùng cho WS / logging

            try {
                // --- B1: Lấy URL từ payload ---
                JSONObject payload = new JSONObject(task.getPayloadJson());
                String url = payload.optString("url", null);

                if (url == null ||  url.trim().isEmpty()) {
                    throw new IllegalArgumentException("Payload thiếu trường 'url'");
                }

                // --- B2: Cào & phân tích ---
                JSONObject analysisResult = scrapeService.fetchAndAnalyze(url);

                // --- B3: Tạo PDF ---
                String pdfDirectory = "D:/webscraper_results/";   // NHỚ tạo thư mục này
                String pdfFileName = "task_" + taskId + ".pdf";
                String fullPdfPath = pdfDirectory + pdfFileName;

                pdfService.createPdf(analysisResult, fullPdfPath);

                // --- B4: Lưu result vào DB ---
                Result result = new Result();
                result.setTaskId(taskId);
                result.setMetaJson(analysisResult.toString());
                result.setOutputPath(fullPdfPath);  // lưu đường dẫn file PDF
                resultDAO.saveResult(result);

                // Cập nhật trạng thái DONE
                taskDAO.updateTaskStatusToDone(taskId);
                System.out.println("[Worker] Hoàn thành task: " + taskId);

                // 🔁 LƯU Ý:
                // Ở phiên bản tách service, mình BỎ phần WebSocketService ở đây
                // Worker chỉ update DB. WebApp sẽ tự đọc DB để hiển thị kết quả.
                // Nếu sau này bạn muốn nâng cao, ta có thể thêm cơ chế notify ngược lại.

            } catch (Exception e) {
                String err = e.getMessage();
                try {
                    taskDAO.updateTaskStatusToFailed(taskId, err);
                } catch (Exception ignore) { }

                System.err.println("[Worker] Lỗi khi xử lý task " + taskId + ": " + err);
            }

        } catch (Throwable t) {
            System.err.println("[Worker] Lỗi không mong muốn khi xử lý task " + taskId + ": " + t);
        }
    }
}
