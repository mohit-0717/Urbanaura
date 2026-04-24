package com.urbanaura.urbanaura.repository;

import com.urbanaura.urbanaura.document.AqiLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AqiLogRepository extends MongoRepository<AqiLog, String> {
    List<AqiLog> findByLocalityOrderByTimestampDesc(String locality);
}
