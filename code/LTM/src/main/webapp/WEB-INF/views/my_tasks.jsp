<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Tác Vụ Của Tôi</title>
    <style>
        body { font-family: sans-serif; margin: 24px; }
        table { border-collapse: collapse; width: 100%; margin-top: 12px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; vertical-align: middle; }
        th { background-color: #f6f6f6; }
        .status-PENDING { color: gray; }
        .status-PROCESSING { color: #1f6feb; font-weight: 600; }
        .status-DONE { color: #0a7a0a; font-weight: 700; }
        .status-FAILED { color: #b00020; font-weight: 700; }
        .small { font-size: 0.9em; color: #666; }
        .actions a { text-decoration: none; color: #0366d6; }

        /* style nhẹ cho nút Xoá */
        .btn-danger {
            background-color: #d9534f;
            border: none;
            color: #fff;
            padding: 4px 10px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.9em;
        }
        .btn-danger:hover {
            background-color: #c9302c;
        }
    </style>
</head>
<body>

<h1>Danh Sách Tác Vụ</h1>
<p><a href="${pageContext.request.contextPath}/create_task.jsp">Tạo tác vụ mới</a></p>

<table>
    <thead>
        <tr>
            <th style="width: 80px;">ID</th>
            <th>URL</th>
            <th style="width: 140px;">Trạng thái</th>
            <th style="width: 180px;">Ngày tạo</th>
            <th style="width: 200px;">Hành động</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${tasks}" var="task">
            <tr class="task-row" data-task-id="${task.id}">
                <td>${task.id}</td>
                <td class="task-url">${task.displayUrl}</td>
                <td class="task-status">
                    <span class="status-${task.status}">${task.status}</span>
                </td>
                <td class="task-created small">${task.createdAt}</td>
                <td class="task-actions actions">
                    <!-- Hành động cũ -->
                    <c:if test="${task.status == 'DONE'}">
                        <c:url var="detailUrl" value="/task-detail">
                            <c:param name="id" value="${task.id}" />
                        </c:url>
                        <a href="${detailUrl}">Xem chi tiết</a>
                    </c:if>
                    <c:if test="${task.status == 'FAILED'}">
                        <span class="small">Lỗi: ${task.error}</span>
                    </c:if>

                    <!-- Nút XOÁ (luôn hiển thị) -->
                    <form method="post"
                          action="${pageContext.request.contextPath}/delete-task"
                          style="display:inline-block; margin-left:10px;">

                        <input type="hidden" name="id" value="${task.id}" />

                        <button type="submit"
                                class="btn-danger"
                                onclick="return confirm('Bạn có chắc muốn xoá task này?');">
                            Xoá
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty tasks}">
            <tr>
                <td colspan="5">Bạn chưa có tác vụ nào.</td>
            </tr>
        </c:if>
    </tbody>
</table>

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
        statusCell.innerHTML = '<span class="status-' + escapeHtml(st) + '">' + escapeHtml(st) + '</span>';
    }

    // cập nhật actions cell
    var actionsCell = row.querySelector('.task-actions');
    if (actionsCell) {
        actionsCell.innerHTML = '';
        if (data.status === 'DONE') {
            var href = window.__CTX + '/task-detail?id=' + encodeURIComponent(taskId);
            actionsCell.innerHTML = '<a href="' + href + '">Xem chi tiết</a>';
        } else if (data.status === 'FAILED') {
            var err = data.error ? escapeHtml(data.error) : 'Lỗi';
            actionsCell.innerHTML = '<span class="small">Lỗi: ' + err + '</span>';
        } else {
            // PENDING / PROCESSING: hiển thị placeholder
            actionsCell.innerHTML = '<span class="small">--</span>';
        }

        // luôn thêm nút Xoá
        var formHtml =
            '<form method="post" ' +
            '      action="' + window.__CTX + '/delete-task" ' +
            '      style="display:inline-block; margin-left:10px;">' +
            '  <input type="hidden" name="id" value="' + escapeHtml(taskId) + '"/>' +
            '  <button type="submit" class="btn-danger" ' +
            '          onclick="return confirm(\\'Bạn có chắc muốn xoá task này?\\');">' +
            '    Xoá' +
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
                    '<span class="status-' + data.status + '">' + data.status + '</span>';

                // update actions
                const actionsCell = row.querySelector(".task-actions");

                if (data.status === "DONE") {
                    const url = window.__CTX + "/task-detail?id=" + taskId;
                    actionsCell.innerHTML =
                        '<a href="' + url + '">Xem chi tiết</a>' +
                        deleteButtonHtml(taskId);
                } else if (data.status === "FAILED") {
                    actionsCell.innerHTML =
                        '<span class="small">Lỗi: ' + (data.error || "Không rõ") + '</span>' +
                        deleteButtonHtml(taskId);
                } else {
                    actionsCell.innerHTML =
                        '<span class="small">--</span>' +
                        deleteButtonHtml(taskId);
                }
            })
            .catch(err => console.error("Polling error:", err));
    });
}

function deleteButtonHtml(taskId) {
    return (
        '<form method="post" ' +
        '      action="' + window.__CTX + '/delete-task" ' +
        '      style="display:inline-block; margin-left:10px;">' +
        '  <input type="hidden" name="id" value="' + taskId + '"/>' +
        '  <button type="submit" class="btn-danger" ' +
        '          onclick="return confirm(\'Bạn có chắc muốn xoá task này?\');">' +
        '    Xoá' +
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
