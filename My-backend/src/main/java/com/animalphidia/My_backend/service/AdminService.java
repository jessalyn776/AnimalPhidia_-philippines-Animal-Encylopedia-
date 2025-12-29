package com.animalphidia.My_backend.service;

import com.animalphidia.My_backend.model.User;
import com.animalphidia.My_backend.model.UserRole;
import com.animalphidia.My_backend.repository.AnimalRepository;
import com.animalphidia.My_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private VerificationWorkflowService workflowService;

    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();

        long totalUsers = userRepository.count();
        long adminCount = userRepository.countByRole(UserRole.ADMIN);
        long moderatorCount = userRepository.countByRole(UserRole.MODERATOR);
        long contributorCount = userRepository.countByRole(UserRole.CONTRIBUTOR);
        long viewerCount = userRepository.countByRole(UserRole.VIEWER);
        long activeUsers = userRepository.countByAccountStatus(true);
        long verifiedUsers = userRepository.countByEmailVerified(true);

        long totalAnimals = animalRepository.count();
        long verifiedAnimals = animalRepository.countByIsVerified(true);
        long activeAnimals = animalRepository.countByActive(true);

        long pendingSubmissions = workflowService.countPendingSubmissions();
        long filteredSubmissions = workflowService.countFilteredSubmissions();

        stats.put("users", Map.of(
                "total", totalUsers,
                "admins", adminCount,
                "moderators", moderatorCount,
                "contributors", contributorCount,
                "viewers", viewerCount,
                "active", activeUsers,
                "verified", verifiedUsers
        ));

        stats.put("animals", Map.of(
                "total", totalAnimals,
                "verified", verifiedAnimals,
                "active", activeAnimals
        ));

        stats.put("submissions", Map.of(
                "pendingModeration", pendingSubmissions,
                "filteredForAdmin", filteredSubmissions
        ));

        stats.put("system", Map.of(
                "status", "ACTIVE",
                "timestamp", LocalDateTime.now()
        ));

        return stats;
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void deleteAnimal(Integer animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new IllegalArgumentException("Animal not found with ID: " + animalId);
        }

        animalRepository.deleteById(animalId);
    }

    public User updateUserRole(Integer userId, String newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        try {
            UserRole role = UserRole.fromString(newRole);
            user.setRole(role); // ✅ FIXED: Only set role using enum

            return userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + newRole);
        }
    }

    public User deactivateUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.setAccountStatus(false);
        user.setActive(false);

        return userRepository.save(user);
    }

    public User activateUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.setAccountStatus(true);
        user.setActive(true);

        return userRepository.save(user);
    }
}