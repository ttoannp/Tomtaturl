<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tác Vụ Của Tôi - Web Scraper</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
    <style>
        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: var(--spacing-lg);
            flex-wrap: wrap;
            gap: var(--spacing-md);
        }
        
        .status-PENDING { background: var(--status-pending); color: white; }
        .status-PROCESSING { background: var(--status-processing); color: white; }
        .status-DONE { background: var(--status-done); color: white; }
        .status-FAILED { background: var(--status-failed); color: white; }
        
        .task-actions {
            display: flex;
            align-items: center;
            gap: var(--spacing-sm);
            flex-wrap: wrap;
        }
        
        .empty-state {
            text-align: center;
            padding: var(--spacing-xl);
            color: var(--text-secondary);
        }
        
        .empty-state-icon {
            font-size: 4rem;
            margin-bottom: var(--spacing-md);
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand">Web Scraper</a>
            <div class="flex gap-2">
                <a href="${pageContext.request.contextPath}/" class="btn-home">Trang chủ</a>
                <a href="${pageContext.request.contextPath}/create_task.jsp" class="btn btn-primary">+ Tạo tác vụ mới</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="page-header">
            <h1 style="margin: 0;">Danh Sách Tác Vụ</h1>
        </div>

        <c:choose>
            <c:when test="${empty tasks || tasks.size() == 0}">
                <div class="card empty-state">
                    <div class="empty-state-icon">📋</div>
                    <h3>Chưa có tác vụ nào</h3>
                    <p>Bắt đầu bằng cách tạo một tác vụ mới để phân tích trang web!</p>
                    <a href="${pageContext.request.contextPath}/create_task.jsp" class="btn btn-primary" style="margin-top: var(--spacing-md);">Tạo tác vụ đầu tiên</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th style="width: 80px;">ID</th>
                                <th>URL</th>
                                <th style="width: 150px;">Trạng thái</th>
                                <th style="width: 200px;">Ngày tạo</th>
                                <th style="width: 180px;">Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${tasks}" var="task">
                                <tr class="task-row" data-task-id="${task.id}">
                                    <td>${task.id}</td>
                                    <td>
                                        <a href="${task.displayUrl}" target="_blank" style="color: var(--primary); word-break: break-all;">
                                            ${task.displayUrl}
                                        </a>
                                    </td>
                                    <td class="task-status">
                                        <span class="badge status-${task.status}">
                                            ${task.status}
                                        </span>
                                    </td>
                                    <td>${task.createdAt}</td>
                                    <td class="task-actions">
                                        <c:if test="${task.status == 'DONE'}">
                                            <a class="btn btn-outline" href="${pageContext.request.contextPath}/task-detail?id=${task.id}" style="padding: 0.5rem 1rem; font-size: 0.875rem;">
                                                Xem chi tiết
                                            </a>
                                        </c:if>
                                        <c:if test="${task.status == 'FAILED'}">
                                            <span style="color: var(--status-failed); font-size: 0.875rem;">Lỗi: ${task.error}</span>
                                        </c:if>
                                        <form method="post" action="${pageContext.request.contextPath}/delete-task" style="display:inline-block;">
                                            <input type="hidden" name="id" value="${task.id}">
                                            <button type="submit" class="btn btn-danger" onclick="return confirm('Bạn có chắc muốn xóa tác vụ này?');" style="padding: 0.5rem 1rem; font-size: 0.875rem;">
                                                Xóa
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

<!-- --- Globals from server (current user id) --- -->
<script type="text/javascript">
    // đảm bảo biến có giá trị số (hoặc null)
    window.__CURRENT_USER_ID = ${currentUser != null ? currentUser.id : "null"};
    // context path để build URL trong JS
    window.__CTX = '${pageContext.request.contextPath}';
</script>

<!-- --- Helper: escapeHtml và updateTaskRowSafe --- -->
<script type="text/javascript">
function escapeHtml(s) {
    if (s == null) return '';
    return String(s)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

/**
 * Cập nhật giao diện cho 1 dòng task an toàn.
 * - taskId: id (number or string)
 * - data: object { id, status, ... }
 */
function updateTaskRowSafe(taskId, data) {
    if (taskId == null) return;
    var selector = ".task-row[data-task-id='" + taskId + "']";
    var row = document.querySelector(selector);
    if (!row) {
        console.warn('updateTaskRowSafe: không tìm thấy row cho taskId=', taskId);
        return;
    }

    // cập nhật status cell
    var statusCell = row.querySelector('.task-status');
    if (statusCell) {
        var st = data.status || '';
        statusCell.innerHTML = '<span class="badge status-' + escapeHtml(st) + '">' + escapeHtml(st) + '</span>';
    }

    // cập nhật actions cell
    var actionsCell = row.querySelector('.task-actions');
    if (actionsCell) {
        actionsCell.innerHTML = '';
        if (data.status === 'DONE') {
            var href = window.__CTX + '/task-detail?id=' + encodeURIComponent(taskId);
            actionsCell.innerHTML = '<a class="btn btn-outline" href="' + href + '" style="padding: 0.5rem 1rem; font-size: 0.875rem;">Xem chi tiết</a>';
        } else if (data.status === 'FAILED') {
            var err = data.error ? escapeHtml(data.error) : 'Lỗi';
            actionsCell.innerHTML = '<span style="color: var(--status-failed); font-size: 0.875rem;">Lỗi: ' + err + '</span>';
        }

        // luôn thêm nút Xoá
        var formHtml =
            '<form method="post" ' +
            '      action="' + window.__CTX + '/delete-task" ' +
            '      style="display:inline-block;">' +
            '  <input type="hidden" name="id" value="' + escapeHtml(taskId) + '"/>' +
            '  <button type="submit" class="btn btn-danger" ' +
            '          onclick="return confirm(\\'Bạn có chắc muốn xóa tác vụ này?\\');" ' +
            '          style="padding: 0.5rem 1rem; font-size: 0.875rem;">' +
            '    Xóa' +
            '  </button>' +
            '</form>';

        actionsCell.innerHTML += formHtml;
    }
}
</script>

<!-- --- AJAX Polling Script (thay cho WebSocket) --- -->
<script type="text/javascript">
function updateTaskStatus() {
    const rows = document.querySelectorAll(".task-row[data-task-id]");

    rows.forEach(row => {
        const taskId = row.getAttribute("data-task-id");

        fetch(window.__CTX + "/api/task-status?taskId=" + taskId)
            .then(res => res.json())
            .then(data => {
                if (!data.success) return;

                // update status
                const statusCell = row.querySelector(".task-status");
                statusCell.innerHTML =
                    '<span class="badge status-' + data.status + '">' + data.status + '</span>';

                // update actions
                const actionsCell = row.querySelector(".task-actions");

                if (data.status === "DONE") {
                    const url = window.__CTX + "/task-detail?id=" + taskId;
                    actionsCell.innerHTML =
                        '<a class="btn btn-outline" href="' + url + '" style="padding: 0.5rem 1rem; font-size: 0.875rem;">Xem chi tiết</a>' +
                        deleteButtonHtml(taskId);
                } else if (data.status === "FAILED") {
                    actionsCell.innerHTML =
                        '<span style="color: var(--status-failed); font-size: 0.875rem;">Lỗi: ' + (data.error || "Không rõ") + '</span>' +
                        deleteButtonHtml(taskId);
                } else {
                    actionsCell.innerHTML = deleteButtonHtml(taskId);
                }
            })
            .catch(err => console.error("Polling error:", err));
    });
}

function deleteButtonHtml(taskId) {
    return (
        '<form method="post" ' +
        '      action="' + window.__CTX + '/delete-task" ' +
        '      style="display:inline-block;">' +
        '  <input type="hidden" name="id" value="' + taskId + '"/>' +
        '  <button type="submit" class="btn btn-danger" ' +
        '          onclick="return confirm(\'Bạn có chắc muốn xóa tác vụ này?\');" ' +
        '          style="padding: 0.5rem 1rem; font-size: 0.875rem;">' +
        '    Xóa' +
        '  </button>' +
        '</form>'
    );
}

// Kiểm tra mỗi 3 giây
setInterval(updateTaskStatus, 3000);

// Gọi ngay lúc load trang
updateTaskStatus();
</script>


</body>
</html>
