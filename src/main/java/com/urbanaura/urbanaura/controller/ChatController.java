package com.urbanaura.urbanaura.controller;

import com.urbanaura.urbanaura.service.AiConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class ChatController {

    @Autowired
    private AiConsultantService aiConsultantService;

    @PostMapping("/consult")
    public ResponseEntity<Map<String, String>> consultAi(@RequestBody Map<String, Object> payload,
                                                         Authentication authentication) {
        String query = (String) payload.getOrDefault("query", "");
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));

        if (query.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("response", "Query cannot be empty."));
        }

        String aiResponse = aiConsultantService.generateConsultation(query, isAdmin);
        
        return ResponseEntity.ok(Map.of("response", aiResponse));
    }
}
