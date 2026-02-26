- Web Scraper & URL Content Summarizer

Ứng dụng web cho phép người dùng nhập URL bất kỳ, hệ thống tự động tải nội dung trang, phân tích văn bản, trích xuất từ khóa và tạo bản tóm tắt ngắn gọn.
Hệ thống được thiết kế theo mô hình Web App + Background Worker, đảm bảo xử lý tác vụ nặng bất đồng bộ và có thể mở rộng trong tương lai.

- Mục tiêu dự án

Xây dựng hệ thống phân tích nội dung web theo hướng xử lý nền (asynchronous processing)

Thiết kế pipeline xử lý dữ liệu: Scraping → Phân tích → Tóm tắt → Xuất báo cáo

Tách biệt xử lý web và xử lý nặng để tối ưu hiệu năng

Tạo nền tảng cho các ứng dụng khai thác dữ liệu văn bản (Text Mining / BI)

- Chức năng chính

Người dùng nhập URL cần phân tích

Tạo task xử lý và lưu trạng thái (PENDING, PROCESSING, DONE, FAILED)

Tự động:

Tải nội dung HTML

Trích xuất metadata (title, description, image)

Phân tích từ khóa bằng thuật toán RAKE

Sinh bản tóm tắt 3 câu quan trọng nhất

Tạo file PDF báo cáo kết quả

Cập nhật trạng thái realtime qua WebSocket

- System Architecture

Hệ thống được chia thành 2 module độc lập:

1. Web Application Layer

Jakarta Servlet + JSP

WebSocket (push trạng thái realtime)

DAO Pattern

Quản lý user, task, hiển thị kết quả

Vai trò:

Nhận request từ người dùng
Tạo task và lưu vào database
Hiển thị kết quả và cập nhật trạng thái realtime

2. Background Worker Layer

Ứng dụng Java chạy độc lập (JAR)

Poll task từ database theo trạng thái PENDING

Thực hiện toàn bộ xử lý nặng

Pipeline xử lý:

Lấy task → Cập nhật PROCESSING
Scrape nội dung bằng Jsoup
Phân tích từ khóa (RAKE)
Tính điểm câu → Sinh tóm tắt
Tạo file PDF (iText 7)
Lưu JSON kết quả vào DB
Cập nhật DONE hoặc FAILED

Thiết kế này giúp:

Tách biệt xử lý web và xử lý nền
Không block request người dùng
Dễ scale thêm worker khi số lượng task tăng

- Kiến trúc phân tầng nội bộ

Cả Web App và Worker đều tổ chức theo layered architecture:

Controller → Service → DAO → Database

Controller: xử lý request và điều phối luồng
Service: xử lý nghiệp vụ (scraping, summarizing, PDF generation)
DAO: thao tác với database
Database: lưu trữ Task, Result, User

Thiết kế giúp tách biệt rõ:

Tầng nghiệp vụ
Tầng truy xuất dữ liệu
Tầng trình bày

- Thiết kế dữ liệu

Các bảng chính:

User
Task
Result

Quan hệ:

1 User → N Task
1 Task → 1 Result

Task lưu payload JSON (URL) và trạng thái xử lý
Result lưu JSON kết quả phân tích và đường dẫn file PDF

Cấu trúc hỗ trợ:

Truy vấn lịch sử xử lý
Theo dõi trạng thái task
Phân tích số lượng URL được xử lý
Thống kê tần suất lỗi

- Công nghệ sử dụng

Backend (Web App & Worker)

Java 17
Jakarta Servlet
Jakarta WebSocket
MySQL
Maven

Thư viện

Jsoup (HTML Parsing)
Gson / org.json (JSON Processing)
iText 7 (PDF Generation)

- Ví dụ luồng xử lý

User nhập URL
Hệ thống tạo task trạng thái PENDING
Worker tự động pick task
Phân tích và sinh PDF
Cập nhật DONE và gửi thông báo realtime

Nếu lỗi (URL không truy cập được / timeout) → chuyển FAILED

- Kiến thức & kỹ năng áp dụng

Thiết kế hệ thống xử lý bất đồng bộ (Async Processing)

Tách module Web App và Worker

Xây dựng pipeline xử lý dữ liệu

Áp dụng thuật toán RAKE trong text mining

Làm việc với WebSocket realtime

Sinh báo cáo PDF tự động

Thiết kế kiến trúc phân tầng (Layered Architecture)