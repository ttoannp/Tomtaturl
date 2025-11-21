package model;

import org.json.JSONException; 
import org.json.JSONObject; 
import java.time.LocalDateTime;
public class Task {
private long id;
private long userId;
private String type;
private String payloadJson;
private String status;
private LocalDateTime createdAt;
private LocalDateTime startedAt;
private LocalDateTime finishedAt;
private String error;

// Constructors, Getters, Setters
public Task() {}

public long getId() { return id; }
public void setId(long id) { this.id = id; }
public long getUserId() { return userId; }
public void setUserId(long userId) { this.userId = userId; }
public String getType() { return type; }
public void setType(String type) { this.type = type; }
public String getPayloadJson() { return payloadJson; }
public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
public String getStatus() { return status; }
public void setStatus(String status) { this.status = status; }
public LocalDateTime getCreatedAt() { return createdAt; }
public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
public LocalDateTime getStartedAt() { return startedAt; }
public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
public LocalDateTime getFinishedAt() { return finishedAt; }
public void setFinishedAt(LocalDateTime finishedAt) { this.finishedAt = finishedAt; }
public String getError() { return error; }
public void setError(String error) { this.error = error; }
public String getDisplayUrl() {
    // BƯỚC 1: Kiểm tra đầu vào cơ bản
    if (payloadJson == null || payloadJson.trim().isEmpty() || payloadJson.equalsIgnoreCase("null")) {
        return "[Không có payload]";
    }
    
    try {
        // BƯỚC 2: Parse chuỗi JSON
        JSONObject payload = new JSONObject(payloadJson);
        
        // BƯỚC 3: Kiểm tra xem key "url" có tồn tại và không phải là null không
        if (payload.has("url") && !payload.isNull("url")) {
            return payload.getString("url");
        } else {
            return "[Không có key 'url']";
        }
    } catch (Exception e) { // Bắt tất cả các loại Exception
        // Nếu có bất kỳ lỗi gì xảy ra trong quá trình parse
        System.err.println("Lỗi nghiêm trọng khi parse payloadJson trong Task.java. Payload: " + payloadJson);
        e.printStackTrace(); // In chi tiết lỗi ra console của Tomcat để gỡ lỗi
        return "[Payload bị lỗi]";
    }
}
}
