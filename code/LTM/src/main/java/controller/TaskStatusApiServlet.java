package controller;

import dao.ResultDAO;
import dao.TaskDAO;
import model.Result;
import model.Task;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/task-status")
public class TaskStatusApiServlet extends HttpServlet {

    private final TaskDAO taskDAO = new TaskDAO();
    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String taskIdParam = req.getParameter("taskId");
        resp.setContentType("application/json; charset=UTF-8");

        JSONObject json = new JSONObject();

        try (PrintWriter out = resp.getWriter()) {

            if (taskIdParam == null || taskIdParam.trim().isEmpty()) {
                json.put("success", false);
                json.put("error", "Missing taskId parameter");
                out.print(json.toString());
                return;
            }

            long taskId = Long.parseLong(taskIdParam);

            Task task = taskDAO.getTaskById(taskId);
            if (task == null) {
                json.put("success", false);
                json.put("error", "Task not found");
                out.print(json.toString());
                return;
            }

            json.put("success", true);
            json.put("id", task.getId());
            json.put("status", task.getStatus()); // chú ý: getStatus() phải có

            // Nếu bạn muốn trả thêm PDF path:
            Result result = resultDAO.getResultByTaskId(taskId); // đảm bảo DAO có hàm này
            if (result != null) {
                json.put("hasResult", true);
                json.put("pdfPath", result.getOutputPath());
            } else {
                json.put("hasResult", false);
            }

            out.print(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
