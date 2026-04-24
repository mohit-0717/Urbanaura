package com.urbanaura.urbanaura.service;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.urbanaura.urbanaura.document.AqiLog;
import com.urbanaura.urbanaura.model.Property;
import com.urbanaura.urbanaura.repository.AqiLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class ReportService {

    @Autowired
    private PropertySearchService propertySearchService;
    
    @Autowired
    private AqiLogRepository aqiLogRepository;

    @Value("${urbanaura.green-index.aqi-weight:0.6}")
    private double aqiWeight;

    @Value("${urbanaura.green-index.green-weight:0.4}")
    private double greenWeight;

    // Fixed Thread Pool for concurrency (Scalability requirement CO1)
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public byte[] generateSdgReport() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(36, 36, 50, 36);

        // Apple / Silicon Valley styling
        DeviceRgb offBlack = new DeviceRgb(29, 29, 31);
        DeviceRgb emeraldGreen = new DeviceRgb(16, 185, 129);

        // Title
        Paragraph title = new Paragraph("UrbanAura Sustainability Review")
                .setFontSize(24)
                .setBold()
                .setFontColor(offBlack)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(title);

        Paragraph desc = new Paragraph("This report details the exact SDG 11.7 (Green Index) values calculated across properties utilizing a multi-threaded High-Performance engine.")
                .setFontSize(10)
                .setFontColor(new DeviceRgb(134, 134, 139))
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(30);
        document.add(desc);

        List<Property> allProperties = propertySearchService.findAllProperties();
        
        // Chunk the properties (e.g., 50 properties per thread)
        int chunkSize = 50;
        List<Future<Table>> futures = new ArrayList<>();

        for (int i = 0; i < allProperties.size(); i += chunkSize) {
            int end = Math.min(allProperties.size(), i + chunkSize);
            List<Property> chunk = allProperties.subList(i, end);
            
            // Thread safety: Executor creates isolated Table objects
            Callable<Table> task = () -> buildPropertyTableChunk(chunk);
            futures.add(executorService.submit(task));
        }

        // Wait for all threads and safely append results to main document sequentially
        for (Future<Table> future : futures) {
            Table chunkTable = future.get(); // Blocks until thread finishes
            document.add(chunkTable);
            document.add(new Paragraph("\n"));
        }

        Paragraph footer = new Paragraph("Confidential System Report | UrbanAura Pune | Administrative export")
                .setFontSize(9)
                .setFontColor(emeraldGreen)
                .setTextAlignment(TextAlignment.CENTER)
                .setFixedPosition(36, 20, 523);
        document.add(footer);

        document.close();
        return baos.toByteArray();
    }

    private Table buildPropertyTableChunk(List<Property> properties) {
        float[] columnWidths = {2, 3, 2, 2, 2};
        Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();
        
        // Table Header
        table.addHeaderCell(createHeaderCell("Locality"));
        table.addHeaderCell(createHeaderCell("Property"));
        table.addHeaderCell(createHeaderCell("Park Dist (m)"));
        table.addHeaderCell(createHeaderCell("Live AQI"));
        table.addHeaderCell(createHeaderCell("Green Index"));

        for (Property p : properties) {
            String localityName = p.getLocality() != null ? p.getLocality().getName() : "Unknown";
            Integer dPark = p.getSmartMetric() != null && p.getSmartMetric().getDistToParkM() != null 
                            ? p.getSmartMetric().getDistToParkM() : 5000;
                            
            Double liveAqi = 100.0; // Default fallback
            if(p.getLocality() != null) {
                List<AqiLog> logs = aqiLogRepository.findByLocalityOrderByTimestampDesc(localityName);
                if(!logs.isEmpty() && logs.get(0).getAqiValue() != null) {
                    liveAqi = logs.get(0).getAqiValue();
                }
            }

            // SDG 11 "Green Index" Formula
            // GI = (((100 - A) / 100) * W_aqi) + (((D_max - D_park) / D_max) * W_park)
            double dMax = 5000.0; // Assume 5km max penalty
            double cappedParkDist = Math.min(dPark, dMax);
            
            double giAqiComponent = ((100.0 - liveAqi) / 100.0) * (aqiWeight * 100.0);
            double giParkComponent = ((dMax - cappedParkDist) / dMax) * (greenWeight * 100.0);
            double greenIndex = giAqiComponent + giParkComponent;
            
            // Bounds check 0-100
            greenIndex = Math.max(0, Math.min(100, greenIndex));

            table.addCell(createCell(localityName));
            table.addCell(createCell(p.getTitle()));
            table.addCell(createCell(String.valueOf(dPark)));
            table.addCell(createCell(String.format("%.1f", liveAqi)));
            
            // Format GI cell specifically for the "impact" vibe
            Cell giCell = new Cell().add(new Paragraph(String.format("%.1f", greenIndex)).setBold());
            if (greenIndex > 70) {
                giCell.setFontColor(new DeviceRgb(16, 185, 129)); // Emerald Green
            } else if (greenIndex < 40) {
                giCell.setFontColor(new DeviceRgb(255, 59, 48)); // Red
            }
            giCell.setTextAlignment(TextAlignment.CENTER);
            giCell.setPadding(5);
            table.addCell(giCell);
        }
        
        return table;
    }

    private Cell createHeaderCell(String text) {
        return new Cell().add(new Paragraph(text).setBold().setFontSize(10))
                .setBackgroundColor(new DeviceRgb(240, 240, 245))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(8);
    }

    private Cell createCell(String text) {
        return new Cell().add(new Paragraph(text != null ? text : "").setFontSize(9))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(5);
    }
}
