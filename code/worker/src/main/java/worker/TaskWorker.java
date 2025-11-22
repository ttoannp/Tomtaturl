package worker;

import bo.PdfGeneratorService;
import bo.ScrapeService;
import dao.ResultDAO;
import dao.TaskDAO;
import model.Result;
import model.Task;
import org.json.JSONObject;

import java.util.List;

public class TaskWorker {

    private final TaskDAO taskDAO = new TaskDAO();
    private final ResultDAO resultDAO = new ResultDAO();
    private final ScrapeService scrapeService = new ScrapeService();
    private final PdfGeneratorService pdfService = new PdfGeneratorService();
    private final WorkerNotifier notifier = new WorkerNotifier();

    public void startLoop() {
        while (true) {
            try {
                List<Task> pendingTasks = taskDAO.getPendingTasks(5);

                if (pendingTasks.isEmpty()) {
                    Thread.sleep(2000);
                    continue;
                }

                for (Task task : pendingTasks) {
                    processTask(task);
                }

            } catch (Exception e) {
                e.printStackTrace();
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            }
        }
    }

    private void processTask(Task task) {
        long taskId = task.getId();
        long userId = task.getUserId();

        System.out.println("[Worker] Bắt đầu xử lý task " + taskId);

        try {
            taskDAO.updateTaskStatusToProcessing(taskId);

            JSONObject payload = new JSONObject(task.getPayloadJson());
            String url = payload.optString("url", null);

            if (url == null || url.trim().isEmpty()) {
                throw new IllegalArgumentException("Payload thiếu 'url'");
            }

            JSONObject analysisResult = scrapeService.fetchAndAnalyze(url);

            String pdfDirectory = "D:/webscraper_results/";
            String pdfName = "task_" + taskId + ".pdf";
            String pdfPath = pdfDirectory + pdfName;

            pdfService.createPdf(analysisResult, pdfPath);

            Result result = new Result();
            result.setTaskId(taskId);
            result.setMetaJson(analysisResult.toString());
            result.setOutputPath(pdfPath);
            resultDAO.saveResult(result);

            JSONObject msg = new JSONObject();
            msg.put("taskId", taskId);
            msg.put("userId", userId);
            msg.put("status", "DONE");
            msg.put("error", JSONObject.NULL);

            notifier.sendToWebApp(msg);

            taskDAO.updateTaskStatusToDone(taskId);

            System.out.println("[Worker] Xử lý xong task " + taskId);

        } catch (Exception ex) {
            ex.printStackTrace();

            try {
                JSONObject msg = new JSONObject();
                msg.put("taskId", taskId);
                msg.put("userId", userId);
                msg.put("status", "FAILED");
                msg.put("error", ex.getMessage());

                notifier.sendToWebApp(msg);

            } catch (Exception ignored) {}

            taskDAO.updateTaskStatusToFailed(taskId, ex.getMessage());
        }
    }
}
