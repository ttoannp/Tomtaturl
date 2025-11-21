package controller;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        /*** 1) Kiểm tra xác nhận mật khẩu ***/
        if (password == null || confirmPassword == null || !password.equals(confirmPassword)) {
            req.setAttribute("error", "Mật khẩu và xác nhận mật khẩu không trùng khớp!");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        /*** 2) Kiểm tra độ mạnh mật khẩu ***/
        String pwRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,}$";
        if (!password.matches(pwRegex)) {
            req.setAttribute("error",
                    "Mật khẩu phải có ít nhất 6 ký tự, chứa chữ hoa, số và ký tự đặc biệt!");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        /*** 3) Tạo user mới ***/
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password); // bạn đang dùng plain text → giữ nguyên

        /*** 4) Gọi DAO để lưu user vào DB ***/
        if (userDAO.createUser(newUser)) {
            resp.sendRedirect(req.getContextPath() + "/login?success=true");
        } else {
            req.setAttribute("error", "Tên đăng nhập đã tồn tại!");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        }
    }
}
