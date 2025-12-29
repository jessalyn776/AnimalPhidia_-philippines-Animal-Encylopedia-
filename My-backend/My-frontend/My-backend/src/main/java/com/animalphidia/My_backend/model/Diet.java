package com.animalphidia.My_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "diet")
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diet_id")
    private Integer dietId;

    @Column(name = "diet_name", nullable = false, length = 100)
    private String dietName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters and Setters
    public Integer getDietId() { return dietId; }
    public void setDietId(Integer dietId) { this.dietId = dietId; }

    public String getDietName() { return dietName; }
    public void setDietName(String dietName) { this.dietName = dietName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}