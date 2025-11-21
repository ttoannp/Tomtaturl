<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng Nhập - Web Scraper</title>
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
            <h2 class="card-title" style="text-align: center; margin-bottom: 0.5rem;">Đăng Nhập</h2>
            <p style="text-align: center; color: var(--text-secondary);">Chào mừng bạn trở lại!</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        <c:if test="${param.success == 'true'}">
            <div class="alert alert-success">Đăng ký thành công! Vui lòng đăng nhập.</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="username">Tên đăng nhập</label>
                <input type="text" id="username" name="username" placeholder="Nhập tên đăng nhập của bạn" required autofocus>
            </div>
            
            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" placeholder="Nhập mật khẩu của bạn" required>
            </div>
            
            <button type="submit" class="btn btn-primary btn-block">Đăng Nhập</button>
        </form>

        <div class="text-center" style="margin-top: 1.5rem; padding-top: 1.5rem; border-top: 1px solid var(--border);">
            <p style="color: var(--text-secondary);">Chưa có tài khoản? 
                <a href="${pageContext.request.contextPath}/register" style="font-weight: 600;">Đăng ký ngay</a>
            </p>
        </div>
    </div>
</body>
</html>