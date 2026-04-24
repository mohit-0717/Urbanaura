package com.urbanaura.urbanaura.repository;

import com.urbanaura.urbanaura.model.Property;
import com.urbanaura.urbanaura.model.TopSmartPropertyDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    @Override
    @EntityGraph(attributePaths = {"locality", "smartMetric", "quickCommerce"})
    List<Property> findAll();

    @EntityGraph(attributePaths = {"locality", "smartMetric", "quickCommerce"})
    List<Property> findByLocalityId(Long localityId);

    // CO3 Requirement: Native Query bypassing standard mapping
    @Query(value = "SELECT * FROM v_top_smart_properties LIMIT 10", nativeQuery = true)
    List<TopSmartPropertyDto> findTopSmartProperties();
}
