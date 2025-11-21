<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>

<html>
<head>
    <title>Chi Tiết Tác Vụ: ${meta['title']}</title>
    <style>
        body { font-family: sans-serif; margin: 40px; max-width: 900px; }
        .container { border: 1px solid #ccc; padding: 20px; border-radius: 8px; }
        .meta-image { max-width: 300px; float: right; margin-left: 20px; border: 1px solid #eee; }
        h1 { font-size: 24px; }
        h2 { font-size: 18px; border-bottom: 1px solid #eee; padding-bottom: 5px; }
        .summary { font-style: italic; color: #555; }
        ul { padding-left: 20px; }
    </style>
</head>
<body>
    <p><a href="${pageContext.request.contextPath}/my-tasks">&larr; Quay lại danh sách</a></p>

    <div class="container">
        
        <c:if test="${not empty meta['imageUrl']}">
            <%-- SỬA Ở ĐÂY --%>
            <img src="${meta['imageUrl']}" alt="Ảnh đại diện" class="meta-image">
        </c:if>

        <%-- SỬA Ở ĐÂY --%>
        <h1>${meta['title']}</h1>
        <p><strong>URL gốc:</strong> <a href="${meta['url']}" target="_blank">${meta['url']}</a></p>
        <p><strong>Mô tả:</strong> ${meta['description']}</p>
        <p><strong>Số từ:</strong> ${meta['wordCount']}</p>
		<p>
    		<a href="${pageContext.request.contextPath}/download-pdf?taskId=${task.id}" target="_blank">
        Tải về kết quả dưới dạng PDF
    		</a>
		</p>
        <hr>

        <h2>Tóm tắt nội dung</h2>
        <%-- SỬA Ở ĐÂY --%>
        <p class="summary">"${meta['summary']}"</p>

        <h2>Từ khóa chính</h2>
        <ul>
            <%-- SỬA Ở ĐÂY: Truy cập vào mảng keywords --%>
            <c:forEach items="${meta['keywords']}" var="keyword">
                <li>${keyword}</li>
            </c:forEach>
        </ul>

        <h2>Các liên kết tìm thấy (tối đa 20)</h2>
        <ul>
            <%-- SỬA Ở ĐÂY: Truy cập vào mảng links --%>
            <c:forEach items="${meta['links']}" var="link">
                <li><a href="${link}" target="_blank">${link}</a></li>
            </c:forEach>
        </ul>
    </div>

</body>
</html>