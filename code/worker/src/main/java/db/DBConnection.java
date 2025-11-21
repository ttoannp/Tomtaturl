package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // --- THAY ĐỔI CÁC THÔNG SỐ NÀY CHO PHÙ HỢP VỚI CẤU HÌNH CỦA BẠN ---
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/webscraper?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "root"; // Thay bằng username MySQL của bạn
    private static final String JDBC_PASSWORD = "123456"; // Thay bằng password MySQL của bạn
    // ----------------------------------------------------------------

    // Nạp driver của MySQL
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Không thể tìm thấy MySQL JDBC Driver!", e);
        }
    }

    /**
     * Lấy một kết nối đến cơ sở dữ liệu.
     * @return một đối tượng Connection.
     * @throws SQLException nếu kết nối thất bại.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }
}