package com.urbanaura.urbanaura.dto;

public class TopSmartPropertyDto {

    private Long id;
    private String title;
    private Double price;
    private String localityName;
    private Double latitude;
    private Double longitude;
    
    // Quick Commerce Metrics
    private Integer blinkitDist;
    private Integer amazonDist;
    
    // Smart Metrics
    private Integer safetyRating;
    private Integer parkDist;
    private Integer metroDist;
    private Integer hospitalDist;
    private Integer noiseLevelDb;
    private Integer aqiBaseline;
    private Double maintenanceMonthly;
    private Double smartScore;
    
    // Calculated Aura Score
    private Double auraScore;
    private Double latestAqi;
    private Double latestPm25;
    private Double greenIndex;
    private String nearestBlinkit;
    private Integer flipkartDist;

    public TopSmartPropertyDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getLocalityName() { return localityName; }
    public void setLocalityName(String localityName) { this.localityName = localityName; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public Integer getBlinkitDist() { return blinkitDist; }
    public void setBlinkitDist(Integer blinkitDist) { this.blinkitDist = blinkitDist; }
    public Integer getAmazonDist() { return amazonDist; }
    public void setAmazonDist(Integer amazonDist) { this.amazonDist = amazonDist; }
    
    public Integer getSafetyRating() { return safetyRating; }
    public void setSafetyRating(Integer safetyRating) { this.safetyRating = safetyRating; }
    public Integer getParkDist() { return parkDist; }
    public void setParkDist(Integer parkDist) { this.parkDist = parkDist; }
    public Integer getMetroDist() { return metroDist; }
    public void setMetroDist(Integer metroDist) { this.metroDist = metroDist; }
    public Integer getHospitalDist() { return hospitalDist; }
    public void setHospitalDist(Integer hospitalDist) { this.hospitalDist = hospitalDist; }
    public Integer getNoiseLevelDb() { return noiseLevelDb; }
    public void setNoiseLevelDb(Integer noiseLevelDb) { this.noiseLevelDb = noiseLevelDb; }
    public Integer getAqiBaseline() { return aqiBaseline; }
    public void setAqiBaseline(Integer aqiBaseline) { this.aqiBaseline = aqiBaseline; }
    public Double getMaintenanceMonthly() { return maintenanceMonthly; }
    public void setMaintenanceMonthly(Double maintenanceMonthly) { this.maintenanceMonthly = maintenanceMonthly; }
    public Double getSmartScore() { return smartScore; }
    public void setSmartScore(Double smartScore) { this.smartScore = smartScore; }
    
    public Double getAuraScore() { return auraScore; }
    public void setAuraScore(Double auraScore) { this.auraScore = auraScore; }
    public Double getLatestAqi() { return latestAqi; }
    public void setLatestAqi(Double latestAqi) { this.latestAqi = latestAqi; }
    public Double getLatestPm25() { return latestPm25; }
    public void setLatestPm25(Double latestPm25) { this.latestPm25 = latestPm25; }
    public Double getGreenIndex() { return greenIndex; }
    public void setGreenIndex(Double greenIndex) { this.greenIndex = greenIndex; }
    public String getNearestBlinkit() { return nearestBlinkit; }
    public void setNearestBlinkit(String nearestBlinkit) { this.nearestBlinkit = nearestBlinkit; }
    public Integer getFlipkartDist() { return flipkartDist; }
    public void setFlipkartDist(Integer flipkartDist) { this.flipkartDist = flipkartDist; }
}
