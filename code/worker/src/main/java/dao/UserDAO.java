package dao;

import db.DBConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Tìm một user dựa trên username (dùng cho việc đăng nhập).
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    // Bạn cũng có thể lấy cả last_login ở đây nếu cần hiển thị
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean createUser(User user) {
        if (getUserByUsername(user.getUsername()) != null) {
            System.out.println("Username " + user.getUsername() + " đã tồn tại.");
            return false;
        }

        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- PHẦN MỚI ĐƯỢC THÊM VÀO ---
    /**
     * Cập nhật thời gian đăng nhập cuối cùng cho một user.
     * @param userId ID của user cần cập nhật.
     */
    public void updateLastLogin(Long userId) {
        String sql = "UPDATE users SET last_login = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setLong(2, userId);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật last_login cho userId: " + userId);
            e.printStackTrace();
        }
    }
    // --- KẾT THÚC PHẦN THÊM MỚI ---
}