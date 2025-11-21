package controller;


import dao.ResultDAO;
import dao.TaskDAO;
import model.*;
import org.json.JSONObject;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/task-detail")
public class TaskDetailServlet extends HttpServlet {
    private final TaskDAO taskDAO = new TaskDAO();
    private final ResultDAO resultDAO = new ResultDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String taskIdStr = req.getParameter("id");
        
        try {
            long taskId = Long.parseLong(taskIdStr);
            
            // 1. Lấy thông tin task và kết quả từ CSDL
            Task task = taskDAO.getTaskById(taskId);
            Result result = resultDAO.getResultByTaskId(taskId);

            if (task != null && result != null) {
                // 2. Chuyển chuỗi JSON trong CSDL thành đối tượng JSONObject
                //    để JSP có thể dễ dàng truy cập (ví dụ: ${meta.title})
                JSONObject metaJson = new JSONObject(result.getMetaJson());
                
                // 3. Đặt các đối tượng vào request để gửi cho JSP
                req.setAttribute("task", task);
                req.setAttribute("meta", metaJson.toMap());

                // 4. Chuyển tiếp tới trang JSP để hiển thị
                RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/task_detail.jsp");
                dispatcher.forward(req, resp);
                
            } else {
                // Xử lý trường hợp không tìm thấy task hoặc kết quả
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy thông tin cho tác vụ này.");
            }
        } catch (NumberFormatException e) {
            // Xử lý trường hợp ID không phải là số
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID của tác vụ không hợp lệ.");
        }
    }
}
