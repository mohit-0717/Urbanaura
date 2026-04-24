package com.urbanaura.urbanaura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "smart_metrics")
public class SmartMetric {

    @Id
    private Long id; // We use property_id as the ID for a true 1-to-1 or generated. Let's use generated and Map differently or share PK.
    // Actually, @OneToOne with MapsId is best, but let's just make it standard generated ID and link.
    
    @Column(name = "aqi_baseline")
    private Integer aqiBaseline;

    @Column(name = "safety_rating")
    private Integer safetyRating;

    @Column(name = "noise_level_db")
    private Integer noiseLevelDb;

    @Column(name = "dist_to_metro_m")
    private Integer distToMetroM;

    @Column(name = "dist_to_hospital_m")
    private Integer distToHospitalM;

    @Column(name = "dist_to_park_m")
    private Integer distToParkM;

    @Column(name = "maintenance_monthly")
    private Double maintenanceMonthly;

    @Column(name = "smart_score")
    private Double smartScore;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "property_id")
    private Property property;

    public SmartMetric() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getAqiBaseline() { return aqiBaseline; }
    public void setAqiBaseline(Integer aqiBaseline) { this.aqiBaseline = aqiBaseline; }
    public Integer getSafetyRating() { return safetyRating; }
    public void setSafetyRating(Integer safetyRating) { this.safetyRating = safetyRating; }
    public Integer getNoiseLevelDb() { return noiseLevelDb; }
    public void setNoiseLevelDb(Integer noiseLevelDb) { this.noiseLevelDb = noiseLevelDb; }
    public Integer getDistToMetroM() { return distToMetroM; }
    public void setDistToMetroM(Integer distToMetroM) { this.distToMetroM = distToMetroM; }
    public Integer getDistToHospitalM() { return distToHospitalM; }
    public void setDistToHospitalM(Integer distToHospitalM) { this.distToHospitalM = distToHospitalM; }
    public Integer getDistToParkM() { return distToParkM; }
    public void setDistToParkM(Integer distToParkM) { this.distToParkM = distToParkM; }
    public Double getMaintenanceMonthly() { return maintenanceMonthly; }
    public void setMaintenanceMonthly(Double maintenanceMonthly) { this.maintenanceMonthly = maintenanceMonthly; }
    public Double getSmartScore() { return smartScore; }
    public void setSmartScore(Double smartScore) { this.smartScore = smartScore; }
    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }
}
