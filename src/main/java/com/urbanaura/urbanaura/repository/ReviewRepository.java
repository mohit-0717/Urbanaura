package com.urbanaura.urbanaura.repository;

import com.urbanaura.urbanaura.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByLocalityId(Long localityId);
}
