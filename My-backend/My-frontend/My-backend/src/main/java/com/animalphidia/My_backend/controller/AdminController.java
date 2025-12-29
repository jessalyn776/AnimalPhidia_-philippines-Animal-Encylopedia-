package com.animalphidia.My_backend.controller;

import com.animalphidia.My_backend.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        try {
            Map<String, Object> stats = statsService.getDashboardStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Failed to fetch dashboard stats",
                    "message", e.getMessage(),
                    "timestamp", java.time.LocalDateTime.now().toString()
            ));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        try {
            Map<String, Object> stats = statsService.getDashboardStats();
            boolean healthy = (Long) stats.get("totalAnimals") >= 0;

            return ResponseEntity.ok(Map.of(
                    "status", healthy ? "HEALTHY" : "DEGRADED",
                    "database", "CONNECTED",
                    "viewAvailable", true,
                    "timestamp", java.time.LocalDateTime.now().toString(),
                    "stats", stats
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                    "status", "UNHEALTHY",
                    "database", "DISCONNECTED",
                    "viewAvailable", false,
                    "timestamp", java.time.LocalDateTime.now().toString(),
                    "error", e.getMessage()
            ));
        }
    }
}