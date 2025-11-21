<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chào mừng đến với Web Scraper</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand">Web Scraper</a>
        </div>
    </nav>

    <div class="hero">
        <h1>Hệ thống Web Scraper & Phân tích Nội dung</h1>
        <p>Tự động tải, trích xuất và tóm tắt thông tin từ bất kỳ trang web nào.</p>
    </div>

    <div class="container">
        <h2 class="text-center" style="font-size: 2rem; margin-bottom: 2rem; color: var(--text-primary);">Tính năng chính</h2>
        
        <div class="features">
            <div class="feature-card">
                <div class="feature-icon">📊</div>
                <h3>Trích xuất Metadata</h3>
                <p>Tự động lấy Tiêu đề, Mô tả, Ảnh đại diện và các thông tin quan trọng khác từ bất kỳ trang web nào.</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon">🔑</div>
                <h3>Phân tích Từ khóa</h3>
                <p>Tìm ra các từ khóa chính trong nội dung văn bản một cách thông minh bằng thuật toán RAKE.</p>
            </div>
            <div class="feature-card">
                <div class="feature-icon">📝</div>
                <h3>Tóm tắt Văn bản</h3>
                <p>Tạo ra một đoạn tóm tắt ngắn gọn và chính xác cho các bài viết dài, giúp tiết kiệm thời gian đọc.</p>
            </div>
        </div>

        <div class="text-center" style="margin-top: 3rem;">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-primary" style="margin-right: 1rem; padding: 1rem 2rem; font-size: 1.1rem;">Đăng Nhập</a>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-secondary" style="padding: 1rem 2rem; font-size: 1.1rem;">Đăng Ký Miễn Phí</a>
        </div>
    </div>
    
    <footer class="footer">
        <p>© 2025 Đồ án JSP/Servlet - Web Scraper Application</p>
    </footer>
</body>
</html>