package com.urbanaura.urbanaura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "quick_commerce")
public class QuickCommerce {

    @Id
    private Long id;

    @Column(name = "nearest_blinkit")
    private String nearestBlinkit;

    @Column(name = "blinkit_distance_m")
    private Integer blinkitDistanceM;

    @Column(name = "amazon_distance_m")
    private Integer amazonDistanceM;

    @Column(name = "flipkart_distance_m")
    private Integer flipkartDistanceM;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "property_id")
    private Property property;

    public QuickCommerce() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNearestBlinkit() { return nearestBlinkit; }
    public void setNearestBlinkit(String nearestBlinkit) { this.nearestBlinkit = nearestBlinkit; }
    public Integer getBlinkitDistanceM() { return blinkitDistanceM; }
    public void setBlinkitDistanceM(Integer blinkitDistanceM) { this.blinkitDistanceM = blinkitDistanceM; }
    public Integer getAmazonDistanceM() { return amazonDistanceM; }
    public void setAmazonDistanceM(Integer amazonDistanceM) { this.amazonDistanceM = amazonDistanceM; }
    public Integer getFlipkartDistanceM() { return flipkartDistanceM; }
    public void setFlipkartDistanceM(Integer flipkartDistanceM) { this.flipkartDistanceM = flipkartDistanceM; }
    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }
}
