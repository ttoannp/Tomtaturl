<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tạo Tác Vụ Mới - Web Scraper</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand">Web Scraper</a>
            <div class="flex gap-2">
                <a href="${pageContext.request.contextPath}/" class="btn-home">Trang chủ</a>
                <a href="${pageContext.request.contextPath}/my-tasks" class="btn btn-outline">Danh sách tác vụ</a>
            </div>
        </div>
    </nav>

    <div class="container-sm">
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Tạo Tác Vụ Mới</h2>
                <p style="color: var(--text-secondary); margin-top: 0.5rem;">Nhập URL của trang web bạn muốn phân tích. Hệ thống sẽ tự động trích xuất metadata, từ khóa và tạo tóm tắt.</p>
            </div>

            <form action="${pageContext.request.contextPath}/create-task" method="POST">
                <div class="form-group">
                    <label for="url">URL trang web</label>
                    <input type="url" id="url" name="url" placeholder="https://vnexpress.net/..." required autofocus>
                    <small style="color: var(--text-secondary); margin-top: 0.5rem; display: block;">
                        Ví dụ: https://vnexpress.net/tin-tuc/the-gioi/...
                    </small>
                </div>
                
                <button type="submit" class="btn btn-primary btn-block">
                    🚀 Bắt đầu Phân Tích
                </button>
            </form>

            <div style="margin-top: 2rem; padding-top: 1.5rem; border-top: 1px solid var(--border);">
                <a href="${pageContext.request.contextPath}/my-tasks" class="link-secondary">
                    ← Quay lại danh sách tác vụ
                </a>
            </div>
        </div>
    </div>
</body>
</html>