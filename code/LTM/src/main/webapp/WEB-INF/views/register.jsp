<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Ký - Web Scraper</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand">Web Scraper</a>
            <a href="${pageContext.request.contextPath}/" class="btn-home">Trang chủ</a>
        </div>
    </nav>

    <div class="form-container">
        <div class="card-header">
            <h2 class="card-title" style="text-align: center; margin-bottom: 0.5rem;">Đăng Ký Tài Khoản</h2>
            <p style="text-align: center; color: var(--text-secondary);">Tạo tài khoản mới để bắt đầu sử dụng</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="form-group">
                <label for="username">Tên đăng nhập</label>
                <input type="text" id="username" name="username" placeholder="Chọn tên đăng nhập" required autofocus>
            </div>
            
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" placeholder="your.email@example.com" required>
            </div>

            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" placeholder="Tạo mật khẩu mạnh" required>
            </div>
            
            <div class="form-group">
                <label for="confirmpassword">Xác nhận mật khẩu</label>
                <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Xác nhận mật khẩu" required>
            </div>
            
            <button type="submit" class="btn btn-primary btn-block">Đăng Ký</button>
        </form>

        <div class="text-center" style="margin-top: 1.5rem; padding-top: 1.5rem; border-top: 1px solid var(--border);">
            <p style="color: var(--text-secondary);">Đã có tài khoản? 
                <a href="${pageContext.request.contextPath}/login" style="font-weight: 600;">Đăng nhập ngay</a>
            </p>
        </div>
    </div>
</body>
</html>```
