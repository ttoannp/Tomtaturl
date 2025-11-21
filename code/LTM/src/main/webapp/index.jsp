<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Chào mừng đến với Web Scraper</title>
    <style>
        body { font-family: sans-serif; background-color: #f4f7f6; color: #333; text-align: center; margin: 0; padding: 0; }
        .hero { background-color: #007bff; color: white; padding: 60px 20px; }
        .hero h1 { font-size: 48px; margin: 0; }
        .hero p { font-size: 20px; }
        .container { padding: 40px 20px; }
        .features { display: flex; justify-content: center; gap: 20px; margin-top: 30px; }
        .feature { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); width: 250px; }
        .actions a { text-decoration: none; color: white; background-color: #28a745; padding: 15px 30px; font-size: 18px; border-radius: 5px; margin: 0 10px; }
        .actions a.login { background-color: #17a2b8; }
        footer { margin-top: 40px; color: #777; }
    </style>
</head>
<body>

    <div class="hero">
        <h1>Hệ thống Web Scraper & Phân tích Nội dung</h1>
        <p>Tự động tải, trích xuất và tóm tắt thông tin từ bất kỳ trang web nào.</p>
    </div>

    <div class="container">
        <h2>Tính năng chính</h2>
        <div class="features">
            <div class="feature">
                <h3>Trích xuất Metadata</h3>
                <p>Tự động lấy Tiêu đề, Mô tả, Ảnh đại diện và các thông tin quan trọng khác.</p>
            </div>
            <div class="feature">
                <h3>Phân tích Từ khóa</h3>
                <p>Tìm ra các từ khóa chính trong nội dung văn bản một cách thông minh.</p>
            </div>
            <div class="feature">
                <h3>Tóm tắt Văn bản</h3>
                <p>Tạo ra một đoạn tóm tắt ngắn gọn cho các bài viết dài.</p>
            </div>
        </div>

        <div class="actions" style="margin-top: 50px;">
            <a href="${pageContext.request.contextPath}/login" class="login">Đăng Nhập</a>
            <a href="${pageContext.request.contextPath}/register">Đăng Ký Miễn Phí</a>
        </div>
    </div>
    
    <footer>
        <p>Đồ án JSP/Servlet - 2025</p>
    </footer>

</body>
</html>