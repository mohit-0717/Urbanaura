package com.urbanaura.urbanaura.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.util.List;

@Entity
@Table(name = "localities")
public class Locality implements Persistable<Long> {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    private String city;
    private Double latitude;
    private Double longitude;

    @Column(name = "safety_score")
    private Double safetyScore; // Aggregated score from reviews

    @OneToMany(mappedBy = "locality", cascade = CascadeType.ALL)
    private List<Property> properties;

    @OneToMany(mappedBy = "locality", cascade = CascadeType.ALL)
    private List<Amenity> amenities;

    @OneToMany(mappedBy = "locality", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @Transient
    private boolean isNew = true;

    public Locality() {}

    // Getters and setters
    @Override
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getSafetyScore() { return safetyScore; }
    public void setSafetyScore(Double safetyScore) { this.safetyScore = safetyScore; }
    public List<Property> getProperties() { return properties; }
    public void setProperties(List<Property> properties) { this.properties = properties; }
    public List<Amenity> getAmenities() { return amenities; }
    public void setAmenities(List<Amenity> amenities) { this.amenities = amenities; }
    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void markPersisted() {
        this.isNew = false;
    }

    @PostLoad
    @PostPersist
    void markNotNew() {
        markPersisted();
    }
}
