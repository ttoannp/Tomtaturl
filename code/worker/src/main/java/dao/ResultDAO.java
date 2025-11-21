package dao;



import db.DBConnection;
import model.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class ResultDAO {

    /**
     * Lưu kết quả phân tích vào CSDL.
     * @param result Đối tượng Result chứa taskId, metaJson, v.v.
     */
    public void saveResult(Result result) {
        String sql = "INSERT INTO results (task_id, meta_json, output_path) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, result.getTaskId());
            pstmt.setString(2, result.getMetaJson());
            pstmt.setString(3, result.getOutputPath());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Result getResultByTaskId(long taskId) {
        String sql = "SELECT * FROM results WHERE task_id = ?";
        Result result = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, taskId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result = mapResultSetToResult(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    private Result mapResultSetToResult(ResultSet rs) throws SQLException {
        Result result = new Result();
        result.setId(rs.getLong("id"));
        result.setTaskId(rs.getLong("task_id"));
        result.setMetaJson(rs.getString("meta_json"));
        result.setOutputPath(rs.getString("output_path"));
        result.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return result;
    }
}
