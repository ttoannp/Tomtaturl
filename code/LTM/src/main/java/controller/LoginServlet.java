package controller;

import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import socket.LoginClient;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userDAO.getUserByUsername(username);

        if (user != null && user.getPassword().equals(password)) {

            // Lưu session để webapp nhận dạng user
            HttpSession session = req.getSession();
            session.setAttribute("currentUser", user);

            // Gửi login event sang Worker bằng socket (không blocking)
            new Thread(() -> {
                try {
                    LoginClient.sendLogin(user.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // Redirect vào trang tác vụ ngay lập tức
            resp.sendRedirect(req.getContextPath() + "/my-tasks");
            return;
        }

        // Sai thông tin đăng nhập
        req.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }
}
