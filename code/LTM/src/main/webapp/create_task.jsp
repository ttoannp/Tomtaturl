<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tạo Tác Vụ Mới</title>
    <style>
        body { font-family: sans-serif; margin: 40px; }
        form { width: 600px; }
        input[type=text] { width: 100%; padding: 8px; font-size: 16px; }
        input[type=submit] { padding: 10px 20px; font-size: 16px; margin-top: 10px; cursor: pointer; }
    </style>
</head>
<body>
    <h1>Hệ thống Web Scraper</h1>
    <p>Nhập một URL để bắt đầu phân tích. Kết quả sẽ được xử lý ngầm.</p>

    <form action="${pageContext.request.contextPath}/create-task" method="POST">
        <label for="url">URL:</label><br>
        <input type="text" id="url" name="url" placeholder="https://vnexpress.net/..." required>
        <br>
        <input type="submit" value="Bắt đầu Phân Tích">
    </form>
    
    <p style="margin-top: 20px;">
        <a href="${pageContext.request.contextPath}/my-tasks">Xem danh sách tác vụ của tôi</a>
    </p>

</body>
</html>