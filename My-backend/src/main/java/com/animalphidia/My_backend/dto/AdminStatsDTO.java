package com.animalphidia.My_backend.dto;

import java.time.LocalDateTime;

public class AdminStatsDTO {
    private Long totalUsers;
    private Long activeUsers;
    private Long totalAnimals;
    private Long activeAnimals;
    private Long verifiedAnimals;
    private Long newAnimalsToday;
    private Long newUsersToday;
    private Long endangeredCount;
    private Long protectedCount;
    private Long adminCount;
    private Long moderatorCount;
    private Long contributorCount;
    private LocalDateTime lastUpdated;

    // Constructors
    public AdminStatsDTO() {}

    public AdminStatsDTO(Long totalUsers, Long activeUsers, Long totalAnimals, Long activeAnimals,
                         Long verifiedAnimals, Long newAnimalsToday, Long newUsersToday,
                         Long endangeredCount, Long protectedCount, Long adminCount,
                         Long moderatorCount, Long contributorCount, LocalDateTime lastUpdated) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.totalAnimals = totalAnimals;
        this.activeAnimals = activeAnimals;
        this.verifiedAnimals = verifiedAnimals;
        this.newAnimalsToday = newAnimalsToday;
        this.newUsersToday = newUsersToday;
        this.endangeredCount = endangeredCount;
        this.protectedCount = protectedCount;
        this.adminCount = adminCount;
        this.moderatorCount = moderatorCount;
        this.contributorCount = contributorCount;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public Long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Long totalUsers) { this.totalUsers = totalUsers; }

    public Long getActiveUsers() { return activeUsers; }
    public void setActiveUsers(Long activeUsers) { this.activeUsers = activeUsers; }

    public Long getTotalAnimals() { return totalAnimals; }
    public void setTotalAnimals(Long totalAnimals) { this.totalAnimals = totalAnimals; }

    public Long getActiveAnimals() { return activeAnimals; }
    public void setActiveAnimals(Long activeAnimals) { this.activeAnimals = activeAnimals; }

    public Long getVerifiedAnimals() { return verifiedAnimals; }
    public void setVerifiedAnimals(Long verifiedAnimals) { this.verifiedAnimals = verifiedAnimals; }

    public Long getNewAnimalsToday() { return newAnimalsToday; }
    public void setNewAnimalsToday(Long newAnimalsToday) { this.newAnimalsToday = newAnimalsToday; }

    public Long getNewUsersToday() { return newUsersToday; }
    public void setNewUsersToday(Long newUsersToday) { this.newUsersToday = newUsersToday; }

    public Long getEndangeredCount() { return endangeredCount; }
    public void setEndangeredCount(Long endangeredCount) { this.endangeredCount = endangeredCount; }

    public Long getProtectedCount() { return protectedCount; }
    public void setProtectedCount(Long protectedCount) { this.protectedCount = protectedCount; }

    public Long getAdminCount() { return adminCount; }
    public void setAdminCount(Long adminCount) { this.adminCount = adminCount; }

    public Long getModeratorCount() { return moderatorCount; }
    public void setModeratorCount(Long moderatorCount) { this.moderatorCount = moderatorCount; }

    public Long getContributorCount() { return contributorCount; }
    public void setContributorCount(Long contributorCount) { this.contributorCount = contributorCount; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}