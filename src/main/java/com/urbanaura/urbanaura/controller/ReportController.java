package com.urbanaura.urbanaura.controller;

import com.urbanaura.urbanaura.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateReport(Authentication authentication) {
        try {
            boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                    .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
            if (!isAdmin) {
                return ResponseEntity.status(403).body("Admin Access Required".getBytes());
            }

            byte[] pdfBytes = reportService.generateSdgReport();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "UrbanAura_SDG11_Report.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(("Report Generation Failed: " + e.getMessage()).getBytes());
        }
    }
}
