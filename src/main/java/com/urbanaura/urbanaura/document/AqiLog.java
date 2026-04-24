package com.urbanaura.urbanaura.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "aqi_logs")
public class AqiLog {

    @Id
    private String id;
    
    private String locality;
    private Double aqiValue;
    private Double pm25;
    private LocalDateTime timestamp;

    public AqiLog() {
        this.timestamp = LocalDateTime.now();
    }

    public AqiLog(String locality, Double aqiValue, Double pm25) {
        this.locality = locality;
        this.aqiValue = aqiValue;
        this.pm25 = pm25;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getLocality() { return locality; }
    public void setLocality(String locality) { this.locality = locality; }
    public Double getAqiValue() { return aqiValue; }
    public void setAqiValue(Double aqiValue) { this.aqiValue = aqiValue; }
    public Double getPm25() { return pm25; }
    public void setPm25(Double pm25) { this.pm25 = pm25; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
