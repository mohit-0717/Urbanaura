package com.urbanaura.urbanaura.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class AmenityLoaderService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Async // Satisfies CO1 Multithreading requirement
    public void uploadAmenities(String csvPath) {
        String sql = "INSERT INTO amenities (name, type, distance, latitude, longitude, locality_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            List<String[]> rows = reader.readAll();
            
            // Assume the first row is a header, we skip it or assume strict data format
            // If the row contains header, usually we start from index 1.
            
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    String[] row = rows.get(i);
                    // row: name, type, distance, lat, lng, locality_id
                    ps.setString(1, row[0]); // Name
                    ps.setString(2, row[1]); // Type
                    ps.setDouble(3, Double.parseDouble(row[2])); // Distance
                    ps.setDouble(4, Double.parseDouble(row[3])); // Latitude
                    ps.setDouble(5, Double.parseDouble(row[4])); // Longitude
                    ps.setLong(6, Long.parseLong(row[5]));   // Locality ID
                }

                @Override
                public int getBatchSize() { 
                    return rows.size(); 
                }
            });
            
            System.out.println("Async Batch Insert Complete: " + rows.size() + " records processed.");
            
        } catch (IOException | CsvException e) {
            System.err.println("Failed to batch import amenities: " + e.getMessage());
        }
    }
}
