package dao;

import db.DBConnection;
import model.Task;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class TaskDAO {

/**
 * Tạo một task mới và trả về ID của nó.
 * @param task Đối tượng Task chứa userId và payloadJson
 * @return ID của task vừa được tạo, hoặc -1 nếu thất bại.
 */
public long createTask(Task task) {
    String sql = "INSERT INTO tasks (user_id, payload_json) VALUES (?, ?)";
    long generatedId = -1;
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        pstmt.setLong(1, task.getUserId());
        pstmt.setString(2, task.getPayloadJson());
        
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getLong(1);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return generatedId;
}

/**
 * Lấy danh sách các task của một user cụ thể.
 */
public List<Task> getTasksByUserId(long userId) {
    List<Task> tasks = new ArrayList<>();
    String sql = "SELECT * FROM tasks WHERE user_id = ? ORDER BY created_at DESC";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setLong(1, userId);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return tasks;
}

/**
 * Cập nhật trạng thái task thành PROCESSING.
 */
public void updateTaskStatusToProcessing(long taskId) {
    String sql = "UPDATE tasks SET status = 'PROCESSING', started_at = CURRENT_TIMESTAMP WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setLong(1, taskId);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

/**
 * Cập nhật trạng thái task thành DONE.
 */
public void updateTaskStatusToDone(long taskId) {
    String sql = "UPDATE tasks SET status = 'DONE', finished_at = CURRENT_TIMESTAMP WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setLong(1, taskId);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

/**
 * Cập nhật trạng thái task thành FAILED.
 */
public void updateTaskStatusToFailed(long taskId, String errorMessage) {
    String sql = "UPDATE tasks SET status = 'FAILED', error = ?, finished_at = CURRENT_TIMESTAMP WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, errorMessage);
        pstmt.setLong(2, taskId);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// Helper method để map ResultSet sang đối tượng Task
private Task mapResultSetToTask(ResultSet rs) throws SQLException {
    Task task = new Task();
    task.setId(rs.getLong("id"));
    task.setUserId(rs.getLong("user_id"));
    task.setType(rs.getString("type"));
    task.setPayloadJson(rs.getString("payload_json"));
    task.setStatus(rs.getString("status"));
    task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
    if (rs.getTimestamp("started_at") != null) {
        task.setStartedAt(rs.getTimestamp("started_at").toLocalDateTime());
    }
    if (rs.getTimestamp("finished_at") != null) {
        task.setFinishedAt(rs.getTimestamp("finished_at").toLocalDateTime());
    }
    task.setError(rs.getString("error"));
    return task;
}
//Thêm phương thức này vào trong class TaskDAO.java

public Task getTaskById(long taskId) {
 String sql = "SELECT * FROM tasks WHERE id = ?";
 Task task = null;
 try (Connection conn = DBConnection.getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql)) {
     pstmt.setLong(1, taskId);
     try (ResultSet rs = pstmt.executeQuery()) {
         if (rs.next()) {
             task = mapResultSetToTask(rs); // Tái sử dụng hàm đã viết
         }
     }
 } catch (SQLException e) {
     e.printStackTrace();
 }
 return task;
}

public boolean deleteTaskByIdAndUserId(long taskId, long userId) {
    String sql = "DELETE FROM tasks WHERE id = ? AND user_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setLong(1, taskId);
        pstmt.setLong(2, userId);

        int affected = pstmt.executeUpdate();
        return affected > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

}