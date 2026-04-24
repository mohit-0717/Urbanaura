package com.urbanaura.urbanaura.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "ai_assistant_history")
public class AiAssistantHistory {

    @Id
    private String id;

    private Long userId;
    private String query;
    private String response;
    private LocalDateTime timestamp;

    public AiAssistantHistory() {}

    public AiAssistantHistory(Long userId, String query, String response) {
        this.userId = userId;
        this.query = query;
        this.response = response;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
