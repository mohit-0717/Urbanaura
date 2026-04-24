package com.urbanaura.urbanaura.service;

import com.urbanaura.urbanaura.document.AqiLog;
import com.urbanaura.urbanaura.model.Locality;
import com.urbanaura.urbanaura.model.Property;
import com.urbanaura.urbanaura.repository.AqiLogRepository;
import com.urbanaura.urbanaura.repository.LocalityRepository;
import com.urbanaura.urbanaura.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PropertySearchService {

    @Autowired
    private PropertyRepository propertyRepository;
    
    @Autowired
    private LocalityRepository localityRepository;

    @Autowired
    private AqiLogRepository aqiLogRepository;

    public List<Property> findAllProperties() {
        return propertyRepository.findAll();
    }

    public List<Property> findPropertiesByLocality(Long localityId) {
        return propertyRepository.findByLocalityId(localityId);
    }

    // Hybrid Join: Fetches property from MySQL, gets Locality name, finds latest AQI in MongoDB
    public Map<String, Object> getPropertyWithAqiContext(Long propertyId) {
        Optional<Property> optProp = propertyRepository.findById(propertyId);
        if (optProp.isEmpty()) return null;

        Property property = optProp.get();
        Locality locality = property.getLocality();

        Map<String, Object> result = new HashMap<>();
        result.put("property", property);
        
        if (locality != null) {
            String localityName = locality.getName();
            // Fetch most recent logs from Mongo for this property's locality
            List<AqiLog> logs = aqiLogRepository.findByLocalityOrderByTimestampDesc(localityName);
            if (!logs.isEmpty()) {
                result.put("latest_aqi", logs.get(0)); // Get the top single latest reading
            } else {
                result.put("latest_aqi", null);
            }
        }
        
        return result;
    }
}
