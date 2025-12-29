package com.animalphidia.My_backend.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
public class StaticFileController {

    // ✅ FIXED: Serve homepage at root
    @GetMapping(value = "/home")
    public ResponseEntity<byte[]> getHome() {
        return serveFile("index.html");
    }

    // ✅ FIXED: Serve index.html directly
    @GetMapping(value = "/index")
    public ResponseEntity<byte[]> getIndex() {
        return serveFile("index.html");
    }

    // ✅ FIXED: Serve any HTML file with /page/ prefix
    @GetMapping(value = "/page/{filename}")
    public ResponseEntity<byte[]> getHtml(@PathVariable String filename) {
        if (!filename.endsWith(".html")) {
            filename = filename + ".html";
        }
        return serveFile(filename);
    }

    // ✅ FIXED: Serve CSS files (already works!)
    @GetMapping(value = "/css/{filename}", produces = "text/css")
    public ResponseEntity<byte[]> getCss(@PathVariable String filename) {
        return serveFile("css/" + filename);
    }

    // ✅ FIXED: Serve JS files (already works!)
    @GetMapping(value = "/js/{filename}", produces = "application/javascript")
    public ResponseEntity<byte[]> getJs(@PathVariable String filename) {
        return serveFile("js/" + filename);
    }

    // ✅ FIXED: Serve nested JS files
    @GetMapping(value = "/js/{folder}/{filename}", produces = "application/javascript")
    public ResponseEntity<byte[]> getJsNested(@PathVariable String folder, @PathVariable String filename) {
        return serveFile("js/" + folder + "/" + filename);
    }

    // ✅ Simple test endpoint at /test
    @GetMapping("/test")
    public String test() {
        return "Static file controller is working! Access pages:\n" +
                "- Homepage: /home or /index\n" +
                "- Explore: /page/explore\n" +
                "- Login: /page/login\n" +
                "- All CSS/JS files work!";
    }

    // ✅ ADD THIS: Debug endpoint to check files
    @GetMapping("/debug/files")
    public String debugFiles() {
        StringBuilder result = new StringBuilder();
        result.append("=== Checking Static Files ===\n");

        String[] files = {
                "static/index.html",
                "static/css/main.css",
                "static/js/core/api-service.js",
                "static/pages/explore.html",
                "static/pages/login.html"
        };

        for (String file : files) {
            ClassPathResource resource = new ClassPathResource(file);
            result.append(file).append(": ").append(resource.exists() ? "✅ FOUND" : "❌ NOT FOUND").append("\n");
        }

        return result.toString();
    }

    private ResponseEntity<byte[]> serveFile(String filepath) {
        try {
            System.out.println("🔍 Loading: static/" + filepath);

            ClassPathResource resource = new ClassPathResource("static/" + filepath);
            if (!resource.exists()) {
                System.out.println("❌ Not found: static/" + filepath);
                return ResponseEntity.notFound().build();
            }

            System.out.println("✅ Found: static/" + filepath);
            InputStream inputStream = resource.getInputStream();
            byte[] bytes = StreamUtils.copyToByteArray(inputStream);

            // Set content type
            if (filepath.endsWith(".css")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.valueOf("text/css"))
                        .body(bytes);
            } else if (filepath.endsWith(".js")) {
                return ResponseEntity.ok()
                        .contentType(MediaType.valueOf("application/javascript"))
                        .body(bytes);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body(bytes);
            }

        } catch (Exception e) {
            System.out.println("❌ Error: static/" + filepath + " - " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(("Error: " + e.getMessage()).getBytes());
        }
    }
}