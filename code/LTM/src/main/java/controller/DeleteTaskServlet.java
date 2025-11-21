package controller;

import dao.TaskDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/delete-task")
public class DeleteTaskServlet extends HttpServlet {

    private final TaskDAO taskDAO = new TaskDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User currentUser = (session != null)
                ? (User) session.getAttribute("currentUser")
                : null;

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String taskIdStr = req.getParameter("id");
        if (taskIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu id task");
            return;
        }

        try {
            long taskId = Long.parseLong(taskIdStr);
            long userId = currentUser.getId();

            boolean ok = taskDAO.deleteTaskByIdAndUserId(taskId, userId);

            if (ok) {
                resp.sendRedirect(req.getContextPath() + "/my-tasks");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "Không xoá được task (không tồn tại hoặc không thuộc user này)");
            }

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID task không hợp lệ");
        }
    }
}
