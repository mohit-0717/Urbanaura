package com.urbanaura.urbanaura.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "properties")
public class Property implements Persistable<Long> {

    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;
    
    @Column(nullable = false)
    private Double price;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locality_id", nullable = false)
    private Locality locality;

    @OneToOne(mappedBy = "property", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private SmartMetric smartMetric;

    @OneToOne(mappedBy = "property", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private QuickCommerce quickCommerce;

    @Transient
    private boolean isNew = true;

    public Property() {}

    // Getters and Setters
    @Override
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Locality getLocality() { return locality; }
    public void setLocality(Locality locality) { this.locality = locality; }
    public SmartMetric getSmartMetric() { return smartMetric; }
    public void setSmartMetric(SmartMetric smartMetric) { this.smartMetric = smartMetric; }
    public QuickCommerce getQuickCommerce() { return quickCommerce; }
    public void setQuickCommerce(QuickCommerce quickCommerce) { this.quickCommerce = quickCommerce; }

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
