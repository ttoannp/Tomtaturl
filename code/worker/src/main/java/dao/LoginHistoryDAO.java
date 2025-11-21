// src/main/java/dao/LoginHistoryDAO.java
package dao;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAO dùng để ghi lại lịch sử đăng nhập vào bảng login_history.
 */
public class LoginHistoryDAO {

    private static final String INSERT_SQL =
            "INSERT INTO login_history (user_id, login_time) VALUES (?, NOW())";

    public void logLogin(Long userId) {
        if (userId == null) return;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setLong(1, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("[LoginHistoryDAO] Lỗi ghi login_history cho userId = " + userId);
            e.printStackTrace();
        }
    }
}
