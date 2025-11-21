-- =====================================================
--  DATABASE: webscraper
--  Mục đích: Lưu thông tin người dùng, tác vụ và kết quả
-- =====================================================

-- 1️⃣ Tạo database (nếu chưa có)
CREATE DATABASE IF NOT EXISTS webscraper
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE webscraper;

-- =====================================================
-- 2️⃣ Bảng users
-- =====================================================
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tài khoản mẫu (mật khẩu giả định: 123456 đã hash sẵn)
INSERT INTO users (username, password_hash, email) VALUES
('admin', '123456', 'admin@example.com'),
('user1', '123456', 'user1@example.com');

-- =====================================================
-- 3️⃣ Bảng tasks
-- =====================================================
DROP TABLE IF EXISTS tasks;

CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type ENUM('SCRAPE_URL') DEFAULT 'SCRAPE_URL',
    payload_json JSON NOT NULL,                -- chứa URL, ví dụ: {"url": "https://vnexpress.net/..."}
    status ENUM('PENDING','PROCESSING','DONE','FAILED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP NULL DEFAULT NULL,
    finished_at TIMESTAMP NULL DEFAULT NULL,
    error TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =====================================================
-- 4️⃣ Bảng results
-- =====================================================
DROP TABLE IF EXISTS results;

CREATE TABLE results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id BIGINT NOT NULL,
    meta_json JSON,                            -- chứa metadata + từ khóa + tóm tắt
    output_path VARCHAR(500),                  -- nếu có file kết quả (ảnh, json)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);

-- =====================================================
-- 5️⃣ VIEW gợi ý: task_results_view
-- =====================================================
CREATE OR REPLACE VIEW task_results_view AS
SELECT 
    t.id AS task_id,
    u.username,
    t.status,
    JSON_UNQUOTE(JSON_EXTRACT(t.payload_json, '$.url')) AS url,
    r.meta_json,
    t.created_at,
    t.finished_at
FROM tasks t
LEFT JOIN users u ON t.user_id = u.id
LEFT JOIN results r ON t.id = r.task_id;

-- =====================================================
-- 6️⃣ Dữ liệu mẫu (tùy chọn)
-- =====================================================
INSERT INTO tasks (user_id, payload_json, status) VALUES
(3, '{"url":"https://vnexpress.net/giao-thong"}', 'PENDING'),
(4, '{"url":"https://tuoitre.vn"}', 'DONE');

INSERT INTO results (task_id, meta_json) VALUES
(5, '{
    "title": "Tin mới nhất từ Tuổi Trẻ Online",
    "summary": "Trang tin tức cập nhật các sự kiện, xã hội, thể thao, công nghệ...",
    "keywords": ["tin tức", "Việt Nam", "thể thao", "công nghệ"]
}');
ALTER TABLE users CHANGE password_hash password VARCHAR(255) NOT NULL;
ALTER TABLE users ADD COLUMN last_login TIMESTAMP NULL DEFAULT NULL;

CREATE TABLE login_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    login_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_login_history_user (user_id)
    -- FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

