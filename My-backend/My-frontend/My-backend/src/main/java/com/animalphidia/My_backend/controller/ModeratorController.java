package com.animalphidia.My_backend.controller;

import com.animalphidia.My_backend.dto.AnimalDTO;
import com.animalphidia.My_backend.model.AnimalVerificationWorkflow;
import com.animalphidia.My_backend.service.AnimalService;
import com.animalphidia.My_backend.service.VerificationWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/moderator")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
public class ModeratorController {

    @Autowired
    private VerificationWorkflowService workflowService;

    @Autowired
    private AnimalService animalService;

    @GetMapping("/pending-submissions")
    public ResponseEntity<?> getPendingSubmissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<AnimalVerificationWorkflow> submissions = workflowService.getPendingSubmissions(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("submissions", submissions.getContent());
            response.put("totalPages", submissions.getTotalPages());
            response.put("totalElements", submissions.getTotalElements());
            response.put("currentPage", page);
            response.put("pageSize", size);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch pending submissions");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/filtered-submissions")
    public ResponseEntity<?> getFilteredSubmissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<AnimalVerificationWorkflow> submissions = workflowService.getFilteredSubmissions(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("submissions", submissions.getContent());
            response.put("totalPages", submissions.getTotalPages());
            response.put("totalElements", submissions.getTotalElements());
            response.put("currentPage", page);
            response.put("pageSize", size);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch filtered submissions");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping("/filter/{animalId}")
    public ResponseEntity<?> filterSubmission(
            @PathVariable Integer animalId,
            @RequestParam(required = false) String notes,
            @RequestParam(defaultValue = "true") boolean isFactual) {
        try {
            AnimalVerificationWorkflow workflow = workflowService.moderatorFilter(animalId, notes, isFactual);

            Map<String, Object> response = new HashMap<>();
            response.put("message", isFactual ? "Submission filtered successfully" : "Submission rejected");
            response.put("animalId", animalId);
            response.put("workflowId", workflow.getWorkflowId());
            response.put("status", workflow.getModerationStatus());
            response.put("notes", workflow.getModerationNotes());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid request");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to process submission");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/animals/unverified")
    public ResponseEntity<?> getUnverifiedAnimals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            // This method needs to be added to AnimalService
            // For now, returning empty
            Map<String, Object> response = new HashMap<>();
            response.put("content", new java.util.ArrayList<>());
            response.put("totalPages", 0);
            response.put("totalElements", 0);
            response.put("currentPage", page);
            response.put("pageSize", size);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch unverified animals");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getModeratorDashboard() {
        try {
            Map<String, Object> dashboard = new HashMap<>();

            // Count pending submissions
            long pendingCount = workflowService.countPendingSubmissions();
            long filteredCount = workflowService.countFilteredSubmissions();

            dashboard.put("pendingSubmissions", pendingCount);
            dashboard.put("filteredSubmissions", filteredCount);
            dashboard.put("totalAssignments", pendingCount + filteredCount);
            dashboard.put("lastUpdated", java.time.LocalDateTime.now().toString());

            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to load dashboard");
            error.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}