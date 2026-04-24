package com.urbanaura.urbanaura.repository;

import com.urbanaura.urbanaura.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    List<Amenity> findByLocalityId(Long localityId);
}
