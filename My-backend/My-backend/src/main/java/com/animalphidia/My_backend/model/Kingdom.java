package com.animalphidia.My_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "kingdom")
public class Kingdom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kingdom_id")
    private Integer kingdomId;

    @Column(name = "kingdom_name", nullable = false, length = 100)
    private String kingdomName;

    @Column(name = "scientific_name", length = 150)
    private String scientificName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters and Setters
    public Integer getKingdomId() { return kingdomId; }
    public void setKingdomId(Integer kingdomId) { this.kingdomId = kingdomId; }

    public String getKingdomName() { return kingdomName; }
    public void setKingdomName(String kingdomName) { this.kingdomName = kingdomName; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}