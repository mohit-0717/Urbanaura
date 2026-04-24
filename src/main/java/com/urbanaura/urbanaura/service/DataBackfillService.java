package com.urbanaura.urbanaura.service;

import com.urbanaura.urbanaura.document.AqiLog;
import com.urbanaura.urbanaura.model.Property;
import com.urbanaura.urbanaura.model.QuickCommerce;
import com.urbanaura.urbanaura.model.SmartMetric;
import com.urbanaura.urbanaura.repository.AqiLogRepository;
import com.urbanaura.urbanaura.repository.PropertyRepository;
import com.urbanaura.urbanaura.repository.QuickCommerceRepository;
import com.urbanaura.urbanaura.repository.SmartMetricRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class DataBackfillService {

    private final PropertyRepository propertyRepository;
    private final SmartMetricRepository smartMetricRepository;
    private final QuickCommerceRepository quickCommerceRepository;
    private final AqiLogRepository aqiLogRepository;

    public DataBackfillService(
            PropertyRepository propertyRepository,
            SmartMetricRepository smartMetricRepository,
            QuickCommerceRepository quickCommerceRepository,
            AqiLogRepository aqiLogRepository) {
        this.propertyRepository = propertyRepository;
        this.smartMetricRepository = smartMetricRepository;
        this.quickCommerceRepository = quickCommerceRepository;
        this.aqiLogRepository = aqiLogRepository;
    }

    @Transactional
    public BackfillSummary backfillMissingData() {
        List<Property> properties = propertyRepository.findAll();
        int smartCreated = 0;
        int smartUpdated = 0;
        int quickCreated = 0;
        int quickUpdated = 0;

        Set<String> localities = new LinkedHashSet<>();

        for (Property property : properties) {
            if (property.getLocality() != null && property.getLocality().getName() != null) {
                localities.add(property.getLocality().getName());
            }

            SmartMetric smartMetric = property.getSmartMetric();
            boolean smartWasMissing = smartMetric == null;
            if (smartMetric == null) {
                smartMetric = smartMetricRepository.findById(property.getId()).orElseGet(SmartMetric::new);
                smartMetric.setProperty(property);
                property.setSmartMetric(smartMetric);
            }
            if (fillSmartMetricDefaults(property, smartMetric)) {
                smartMetricRepository.save(smartMetric);
                if (smartWasMissing) {
                    smartCreated++;
                } else {
                    smartUpdated++;
                }
            }

            QuickCommerce quickCommerce = property.getQuickCommerce();
            boolean quickWasMissing = quickCommerce == null;
            if (quickCommerce == null) {
                quickCommerce = quickCommerceRepository.findById(property.getId()).orElseGet(QuickCommerce::new);
                quickCommerce.setProperty(property);
                property.setQuickCommerce(quickCommerce);
            }
            if (fillQuickCommerceDefaults(property, quickCommerce)) {
                quickCommerceRepository.save(quickCommerce);
                if (quickWasMissing) {
                    quickCreated++;
                } else {
                    quickUpdated++;
                }
            }
        }

        int aqiSeeded = seedMissingAqiLogs(localities);
        return new BackfillSummary(properties.size(), smartCreated, smartUpdated, quickCreated, quickUpdated, aqiSeeded);
    }

    private boolean fillSmartMetricDefaults(Property property, SmartMetric smartMetric) {
        boolean changed = false;
        int jitter = jitter(property.getId());

        if (isMissingInt(smartMetric.getAqiBaseline())) {
            smartMetric.setAqiBaseline(65 + (jitter % 30));
            changed = true;
        }
        if (isMissingInt(smartMetric.getSafetyRating())) {
            smartMetric.setSafetyRating(6 + (jitter % 4));
            changed = true;
        }
        if (isMissingInt(smartMetric.getNoiseLevelDb())) {
            smartMetric.setNoiseLevelDb(48 + (jitter % 18));
            changed = true;
        }
        if (isMissingInt(smartMetric.getDistToMetroM())) {
            smartMetric.setDistToMetroM(300 + (jitter * 22));
            changed = true;
        }
        if (isMissingInt(smartMetric.getDistToHospitalM())) {
            smartMetric.setDistToHospitalM(450 + (jitter * 25));
            changed = true;
        }
        if (isMissingInt(smartMetric.getDistToParkM())) {
            smartMetric.setDistToParkM(220 + (jitter * 18));
            changed = true;
        }
        if (isMissingDouble(smartMetric.getMaintenanceMonthly())) {
            double price = property.getPrice() != null ? property.getPrice() : 1.0;
            smartMetric.setMaintenanceMonthly(Math.max(1800.0, Math.min(12000.0, 1700.0 + (price * 1100.0))));
            changed = true;
        }
        if (isMissingDouble(smartMetric.getSmartScore())) {
            double score = calculateSmartScore(
                    smartMetric.getSafetyRating(),
                    smartMetric.getNoiseLevelDb(),
                    smartMetric.getDistToMetroM(),
                    smartMetric.getDistToHospitalM(),
                    smartMetric.getDistToParkM());
            smartMetric.setSmartScore(score);
            changed = true;
        }
        return changed;
    }

    private boolean fillQuickCommerceDefaults(Property property, QuickCommerce quickCommerce) {
        boolean changed = false;
        int jitter = jitter(property.getId());
        String localityName = property.getLocality() != null ? property.getLocality().getName() : "Pune";

        if (quickCommerce.getNearestBlinkit() == null || quickCommerce.getNearestBlinkit().isBlank()) {
            quickCommerce.setNearestBlinkit(localityName + " Hub");
            changed = true;
        }
        if (isMissingInt(quickCommerce.getBlinkitDistanceM())) {
            quickCommerce.setBlinkitDistanceM(260 + (jitter * 14));
            changed = true;
        }
        if (isMissingInt(quickCommerce.getAmazonDistanceM())) {
            quickCommerce.setAmazonDistanceM(420 + (jitter * 17));
            changed = true;
        }
        if (isMissingInt(quickCommerce.getFlipkartDistanceM())) {
            quickCommerce.setFlipkartDistanceM(500 + (jitter * 16));
            changed = true;
        }
        return changed;
    }

    private int seedMissingAqiLogs(Set<String> localities) {
        int seeded = 0;
        for (String locality : localities) {
            if (aqiLogRepository.findByLocalityOrderByTimestampDesc(locality).isEmpty()) {
                int localityJitter = Math.abs(locality.hashCode()) % 25;
                double aqi = 70 + localityJitter;
                double pm25 = 28 + (localityJitter * 0.6);
                aqiLogRepository.save(new AqiLog(locality, aqi, pm25));
                seeded++;
            }
        }
        return seeded;
    }

    private boolean isMissingInt(Integer value) {
        return value == null || value <= 0;
    }

    private boolean isMissingDouble(Double value) {
        return value == null || value <= 0.0;
    }

    private int jitter(Long id) {
        if (id == null) {
            return 7;
        }
        return (int) (Math.abs(id) % 30);
    }

    private double calculateSmartScore(Integer safety, Integer noise, Integer metro, Integer hospital, Integer park) {
        double safetyNorm = ((safety != null ? safety : 6) / 10.0) * 100.0;
        double noiseNorm = Math.max(0.0, 100.0 - ((noise != null ? noise : 58) - 40.0) * 2.0);
        double metroNorm = Math.max(0.0, 100.0 - (metro != null ? metro : 800) / 20.0);
        double hospitalNorm = Math.max(0.0, 100.0 - (hospital != null ? hospital : 1000) / 25.0);
        double parkNorm = Math.max(0.0, 100.0 - (park != null ? park : 900) / 18.0);

        double score = (safetyNorm * 0.30)
                + (noiseNorm * 0.20)
                + (metroNorm * 0.20)
                + (hospitalNorm * 0.15)
                + (parkNorm * 0.15);
        return Math.round(Math.max(0.0, Math.min(100.0, score)) * 100.0) / 100.0;
    }

    public record BackfillSummary(
            int propertiesScanned,
            int smartMetricsCreated,
            int smartMetricsUpdated,
            int quickCommerceCreated,
            int quickCommerceUpdated,
            int aqiSeeded) {
    }
}
