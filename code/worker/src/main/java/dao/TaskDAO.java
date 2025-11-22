package dao;

import db.DBConnection;
import model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    /**
     * Tạo task mới với trạng thái PENDING.
     */
    public long createTask(Task task) {
        String sql = "INSERT INTO tasks (user_id, payload_json, status) VALUES (?, ?, 'PENDING')";
        long generatedId = -1;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, task.getUserId());
            pstmt.setString(2, task.getPayloadJson());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
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
     * Lấy danh sách task của user.
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
     * Worker: Lấy các task PENDING.
     */
    public List<Task> getPendingTasks(int limit) {
        List<Task> list = new ArrayList<>();

        String sql =
            "SELECT * FROM tasks " +
            "WHERE status = 'PENDING' " +
            "ORDER BY created_at ASC " +
            "LIMIT ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTask(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Set status PROCESSING.
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
     * Set status DONE.
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
     * Set status FAILED + error message.
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

    /**
     * Lấy 1 task theo ID.
     */
    public Task getTaskById(long taskId) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        Task task = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, taskId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    task = mapResultSetToTask(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return task;
    }

    /**
     * Xóa task theo ID và user.
     */
    public boolean deleteTaskByIdAndUserId(long taskId, long userId) {
        String sql = "DELETE FROM tasks WHERE id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, taskId);
            pstmt.setLong(2, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // -------------------------
    // MAP RESULTSET → TASK
    // -------------------------
    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setUserId(rs.getLong("user_id"));
        task.setType(rs.getString("type"));
        task.setPayloadJson(rs.getString("payload_json"));
        task.setStatus(rs.getString("status"));

        task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        Timestamp started = rs.getTimestamp("started_at");
        if (started != null) task.setStartedAt(started.toLocalDateTime());

        Timestamp finished = rs.getTimestamp("finished_at");
        if (finished != null) task.setFinishedAt(finished.toLocalDateTime());

        task.setError(rs.getString("error"));

        return task;
    }
}
