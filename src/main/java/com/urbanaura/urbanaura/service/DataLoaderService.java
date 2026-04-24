package com.urbanaura.urbanaura.service;

import com.urbanaura.urbanaura.model.*;
import com.urbanaura.urbanaura.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataLoaderService {
    private static final Logger log = LoggerFactory.getLogger(DataLoaderService.class);

    @Autowired private LocalityRepository localityRepo;
    @Autowired private PropertyRepository propertyRepo;
    @Autowired private SmartMetricRepository smartRepo;
    @Autowired private QuickCommerceRepository qcRepo;

    public void loadAllData() {
        log.info("Starting Excel load job with safe parsing");
        try (FileInputStream file = openWorkbookFile();
             Workbook workbook = new XSSFWorkbook(file)) {
            DataFormatter df = new DataFormatter();
            Map<Long, Locality> localitiesById = new HashMap<>();
            Map<Long, Property> propertiesById = new HashMap<>();

            // 2. Localities (Sheet 1)
            Sheet locSheet = workbook.getSheet("Localities");
            if (locSheet != null) {
                for (int i = 1; i <= locSheet.getLastRowNum(); i++) {
                    Row row = locSheet.getRow(i);
                    if (row == null || row.getCell(0) == null) continue;
                    try {
                        long localityId = parseLong(df.formatCellValue(row.getCell(0)));
                        if (localityId <= 0) {
                            continue;
                        }
                        Locality loc = localitiesById.computeIfAbsent(localityId,
                                id -> localityRepo.findById(id).orElseGet(Locality::new));
                        loc.setId(localityId);
                        loc.setName(df.formatCellValue(row.getCell(1)));
                        loc.setLatitude(parseDouble(df.formatCellValue(row.getCell(2))));
                        loc.setLongitude(parseDouble(df.formatCellValue(row.getCell(3))));
                        loc.setSafetyScore(parseDouble(df.formatCellValue(row.getCell(5))));
                        Locality savedLocality = localityRepo.save(loc);
                        savedLocality.markPersisted();
                        localitiesById.put(localityId, savedLocality);
                    } catch(Exception e) {
                        logRowFailure("Localities", i, e);
                    }
                }
            }

            // 3. Properties (Sheet 0)
            Sheet propSheet = workbook.getSheet("Properties");
            if (propSheet != null) {
                for (int i = 1; i <= propSheet.getLastRowNum(); i++) {
                    Row row = propSheet.getRow(i);
                    if (row == null || row.getCell(0) == null) continue;
                    try {
                        long propertyId = parseLong(df.formatCellValue(row.getCell(0)));
                        long localityId = parseLong(df.formatCellValue(row.getCell(7)));
                        if (propertyId <= 0 || localityId <= 0) {
                            continue;
                        }
                        Locality loc = localitiesById.computeIfAbsent(localityId, id -> localityRepo.findById(id).orElse(null));
                        if (loc == null) {
                            continue;
                        }

                        Property p = propertiesById.computeIfAbsent(propertyId,
                                id -> propertyRepo.findById(id).orElseGet(Property::new));
                        p.setId(propertyId);
                        p.setTitle(df.formatCellValue(row.getCell(1)));
                        p.setPrice(parseDouble(df.formatCellValue(row.getCell(2))));
                        p.setLocality(loc);
                        Property savedProperty = propertyRepo.save(p);
                        savedProperty.markPersisted();
                        propertiesById.put(propertyId, savedProperty);
                    } catch(Exception e) {
                        logRowFailure("Properties", i, e);
                    }
                }
            }

            // 4. Smart_Metrics (Sheet 5)
            Sheet smSheet = workbook.getSheet("Smart_Metrics");
            if (smSheet != null) {
                for (int i = 1; i <= smSheet.getLastRowNum(); i++) {
                    Row row = smSheet.getRow(i);
                    if (row == null || row.getCell(0) == null) continue;
                    try {
                        Long propId = parseLong(df.formatCellValue(row.getCell(0)));
                        Property p = propertiesById.computeIfAbsent(propId, id -> propertyRepo.findById(id).orElse(null));
                        if (p != null) {
                            SmartMetric sm = smartRepo.findById(propId).orElseGet(SmartMetric::new);
                            sm.setProperty(p);
                            sm.setAqiBaseline(parseInt(df.formatCellValue(row.getCell(1))));
                            sm.setSafetyRating(parseInt(df.formatCellValue(row.getCell(2))));
                            sm.setNoiseLevelDb(parseInt(df.formatCellValue(row.getCell(3))));
                            sm.setDistToMetroM(parseInt(df.formatCellValue(row.getCell(4))));
                            sm.setDistToHospitalM(parseInt(df.formatCellValue(row.getCell(5))));
                            sm.setDistToParkM(parseInt(df.formatCellValue(row.getCell(6))));
                            sm.setMaintenanceMonthly(parseDouble(df.formatCellValue(row.getCell(7))));
                            sm.setSmartScore(parseDouble(df.formatCellValue(row.getCell(8))));
                            p.setSmartMetric(sm);
                            smartRepo.save(sm);
                        }
                    } catch(Exception e) {
                        logRowFailure("Smart_Metrics", i, e);
                    }
                }
            }

            // 5. Quick_Commerce (Sheet 6)
            Sheet qcSheet = workbook.getSheet("Quick_Commerce");
            if (qcSheet != null) {
                for (int i = 1; i <= qcSheet.getLastRowNum(); i++) {
                    Row row = qcSheet.getRow(i);
                    if (row == null || row.getCell(0) == null) continue;
                    try {
                        Long propId = parseLong(df.formatCellValue(row.getCell(0)));
                        Property p = propertiesById.computeIfAbsent(propId, id -> propertyRepo.findById(id).orElse(null));
                        if (p != null) {
                            QuickCommerce qc = qcRepo.findById(propId).orElseGet(QuickCommerce::new);
                            qc.setProperty(p);
                            qc.setNearestBlinkit(df.formatCellValue(row.getCell(1)));
                            qc.setBlinkitDistanceM(parseInt(df.formatCellValue(row.getCell(2))));
                            qc.setAmazonDistanceM(parseInt(df.formatCellValue(row.getCell(3))));
                            qc.setFlipkartDistanceM(parseInt(df.formatCellValue(row.getCell(4))));
                            p.setQuickCommerce(qc);
                            qcRepo.save(qc);
                        }
                    } catch(Exception e) {
                        logRowFailure("Quick_Commerce", i, e);
                    }
                }
            }
            log.info("Excel data load completed");

        } catch (Exception e) {
            log.error("Excel data load failed", e);
        }
    }

    private FileInputStream openWorkbookFile() throws IOException {
        try {
            return new FileInputStream(new ClassPathResource("data/UrbanAura_SmartCity_Hub.xlsx").getFile());
        } catch (IOException ignored) {
            return new FileInputStream("src/main/resources/data/UrbanAura_SmartCity_Hub.xlsx");
        }
    }

    private void logRowFailure(String sheetName, int rowIndex, Exception e) {
        log.warn("Skipping {} row {}: {}", sheetName, rowIndex + 1, e.getMessage());
    }

    private long parseLong(String val) {
        if(val == null || val.trim().isEmpty()) return 0L;
        try { return (long) Double.parseDouble(val.trim()); } catch(Exception e) { return 0L; }
    }
    
    private double parseDouble(String val) {
        if(val == null || val.trim().isEmpty()) return 0.0;
        try { return Double.parseDouble(val.trim()); } catch(Exception e) { return 0.0; }
    }

    private int parseInt(String val) {
        if(val == null || val.trim().isEmpty()) return 0;
        try { return (int) Double.parseDouble(val.trim()); } catch(Exception e) { return 0; }
    }
}
