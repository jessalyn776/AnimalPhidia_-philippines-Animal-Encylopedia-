package com.animalphidia.My_backend.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HomeController {

    // API Info endpoint
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(getApiInfo());
    }

    // Root API endpoint
    @GetMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> apiRoot() {
        return ResponseEntity.ok(getApiInfo());
    }

    // Serve the homepage HTML file
    @GetMapping(value = {"/home", "/index"}, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<byte[]> serveHomepage() {
        return serveStaticFile("static/index.html");
    }

    // Serve any HTML page
    @GetMapping(value = "/page/{pageName}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<byte[]> servePage(@PathVariable String pageName) {
        return serveStaticFile("static/" + pageName + ".html");
    }

    // Helper method to serve static files
    private ResponseEntity<byte[]> serveStaticFile(String filePath) {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            InputStream inputStream = resource.getInputStream();
            byte[] bytes = StreamUtils.copyToByteArray(inputStream);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(bytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private Map<String, Object> getApiInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Animalphidia Backend API");
        response.put("status", "RUNNING");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        response.put("service", "Philippine Animal Encyclopedia Backend");

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("health", "/api/health");
        endpoints.put("animals", "/api/animals");
        endpoints.put("auth", "/api/auth");
        endpoints.put("users", "/api/users");
        endpoints.put("search", "/api/search");
        endpoints.put("taxonomy", "/api/taxonomy");
        endpoints.put("documentation", "/swagger-ui.html");
        endpoints.put("homepage", "/api/home (or access /index.html directly)");

        response.put("endpoints", endpoints);

        return response;
    }
}