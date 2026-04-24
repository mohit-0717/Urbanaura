package com.urbanaura.urbanaura.repository;

import com.urbanaura.urbanaura.model.Locality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalityRepository extends JpaRepository<Locality, Long> {
    boolean existsByNameIgnoreCase(String name);
}
