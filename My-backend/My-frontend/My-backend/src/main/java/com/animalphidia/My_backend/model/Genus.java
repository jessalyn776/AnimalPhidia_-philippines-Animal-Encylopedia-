package com.animalphidia.My_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "genus")
public class Genus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genus_id")
    private Integer genusId;

    @Column(name = "genus_name", nullable = false, length = 100)
    private String genusName;

    @Column(name = "family_id")
    private Integer familyId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters and Setters
    public Integer getGenusId() { return genusId; }
    public void setGenusId(Integer genusId) { this.genusId = genusId; }

    public String getGenusName() { return genusName; }
    public void setGenusName(String genusName) { this.genusName = genusName; }

    public Integer getFamilyId() { return familyId; }
    public void setFamilyId(Integer familyId) { this.familyId = familyId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}