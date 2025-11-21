<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi Tiết Tác Vụ: ${meta['title']} - Web Scraper</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
    <style>
        .detail-header {
            margin-bottom: var(--spacing-lg);
            padding-bottom: var(--spacing-lg);
            border-bottom: 2px solid var(--border);
        }
        
        .meta-image {
            max-width: 100%;
            width: 100%;
            max-height: 400px;
            object-fit: cover;
            border-radius: var(--radius-lg);
            margin-bottom: var(--spacing-md);
            box-shadow: var(--shadow-md);
        }
        
        .detail-section {
            margin-bottom: var(--spacing-xl);
            padding-bottom: var(--spacing-lg);
            border-bottom: 1px solid var(--border);
        }
        
        .detail-section:last-child {
            border-bottom: none;
        }
        
        .keyword-tag {
            display: inline-block;
            background: var(--primary);
            color: white;
            padding: 0.5rem 1rem;
            border-radius: var(--radius-md);
            margin: 0.25rem;
            font-size: 0.875rem;
            font-weight: 600;
        }
        
        .link-item {
            padding: var(--spacing-sm);
            margin-bottom: var(--spacing-xs);
            background: var(--bg-primary);
            border-radius: var(--radius-md);
            border-left: 3px solid var(--primary);
        }
        
        .link-item a {
            color: var(--primary);
            word-break: break-all;
        }
        
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: var(--spacing-md);
            margin-top: var(--spacing-md);
        }
        
        .info-item {
            background: var(--bg-primary);
            padding: var(--spacing-md);
            border-radius: var(--radius-md);
        }
        
        .info-label {
            font-size: 0.875rem;
            color: var(--text-secondary);
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.05em;
            margin-bottom: var(--spacing-xs);
        }
        
        .info-value {
            font-size: 1.125rem;
            color: var(--text-primary);
            font-weight: 600;
        }
    </style>
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

    <div class="container">
        <div style="margin-bottom: var(--spacing-md);">
            <a href="${pageContext.request.contextPath}/my-tasks" class="link-secondary" style="display: inline-flex; align-items: center; gap: 0.5rem;">
                ← Quay lại danh sách
            </a>
        </div>

        <div class="card">
            <div class="detail-header">
                <c:if test="${not empty meta['imageUrl']}">
                    <img src="${meta['imageUrl']}" alt="Ảnh đại diện" class="meta-image" onerror="this.style.display='none';">
                </c:if>
                
                <h1 style="font-size: 2rem; margin-bottom: var(--spacing-md); color: var(--text-primary);">
                    ${meta['title']}
                </h1>
                
                <div class="info-grid">
                    <div class="info-item">
                        <div class="info-label">URL gốc</div>
                        <div class="info-value">
                            <a href="${meta['url']}" target="_blank" style="font-size: 0.875rem; word-break: break-all;">
                                ${meta['url']}
                            </a>
                        </div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Số từ</div>
                        <div class="info-value">${meta['wordCount']}</div>
                    </div>
                </div>
                
                <c:if test="${not empty meta['description']}">
                    <div style="margin-top: var(--spacing-md); padding: var(--spacing-md); background: var(--bg-primary); border-radius: var(--radius-md);">
                        <div class="info-label">Mô tả</div>
                        <p style="margin-top: var(--spacing-xs); color: var(--text-primary); line-height: 1.7;">
                            ${meta['description']}
                        </p>
                    </div>
                </c:if>
                
                <div style="margin-top: var(--spacing-md);">
                    <a href="${pageContext.request.contextPath}/download-pdf?taskId=${task.id}" target="_blank" class="btn btn-secondary">
                        📄 Tải về PDF
                    </a>
                </div>
            </div>

            <div class="detail-section">
                <h2 style="font-size: 1.5rem; margin-bottom: var(--spacing-md); color: var(--text-primary);">
                    📝 Tóm tắt nội dung
                </h2>
                <div style="background: var(--bg-primary); padding: var(--spacing-lg); border-radius: var(--radius-md); border-left: 4px solid var(--primary);">
                    <p style="font-style: italic; color: var(--text-primary); line-height: 1.8; margin: 0; font-size: 1.1rem;">
                        "${meta['summary']}"
                    </p>
                </div>
            </div>

            <div class="detail-section">
                <h2 style="font-size: 1.5rem; margin-bottom: var(--spacing-md); color: var(--text-primary);">
                    🔑 Từ khóa chính
                </h2>
                <div>
                    <c:forEach items="${meta['keywords']}" var="keyword">
                        <span class="keyword-tag">${keyword}</span>
                    </c:forEach>
                </div>
            </div>

            <div class="detail-section">
                <h2 style="font-size: 1.5rem; margin-bottom: var(--spacing-md); color: var(--text-primary);">
                    🔗 Các liên kết tìm thấy (tối đa 20)
                </h2>
                <div>
                    <c:choose>
                        <c:when test="${empty meta['links'] || fn:length(meta['links']) == 0}">
                            <p style="color: var(--text-secondary);">Không tìm thấy liên kết nào.</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${meta['links']}" var="link">
                                <div class="link-item">
                                    <a href="${link}" target="_blank">${link}</a>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</body>
</html>