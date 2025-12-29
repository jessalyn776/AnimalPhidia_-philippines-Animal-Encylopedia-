package com.animalphidia.My_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "phylum")
public class Phylum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phylum_id")
    private Integer phylumId;

    @Column(name = "phylum_name", nullable = false, length = 100)
    private String phylumName;

    @Column(name = "kingdom_id")
    private Integer kingdomId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters and Setters
    public Integer getPhylumId() { return phylumId; }
    public void setPhylumId(Integer phylumId) { this.phylumId = phylumId; }

    public String getPhylumName() { return phylumName; }
    public void setPhylumName(String phylumName) { this.phylumName = phylumName; }

    public Integer getKingdomId() { return kingdomId; }
    public void setKingdomId(Integer kingdomId) { this.kingdomId = kingdomId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}