package controller;

import dao.TaskDAO;
import model.Task;
import model.User;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/create-task")
public class CreateTaskServlet extends HttpServlet {

    private final TaskDAO taskDAO = new TaskDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String url = req.getParameter("url");
        if (url == null || url.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/my-tasks");
            return;
        }

        long userId = currentUser.getId();

        Task task = new Task();
        task.setUserId(userId);

        JSONObject payload = new JSONObject();
        payload.put("url", url);
        task.setPayloadJson(payload.toString());

        // trong DAO, createTask cần set status = "PENDING" mặc định
        long newTaskId = taskDAO.createTask(task);
        System.out.println("[WebApp] Tạo task " + newTaskId + " với URL = " + url);

        resp.sendRedirect(req.getContextPath() + "/my-tasks");
    }
}
