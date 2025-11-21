package controller;

import dao.TaskDAO;
import model.Task;
import model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/my-tasks")
public class MyTasksServlet extends HttpServlet {
    private final TaskDAO taskDAO = new TaskDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        long userId = currentUser.getId();
        
        List<Task> taskList = taskDAO.getTasksByUserId(userId);

        req.setAttribute("tasks", taskList);
        
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/my_tasks.jsp");
        dispatcher.forward(req, resp);
    }
}