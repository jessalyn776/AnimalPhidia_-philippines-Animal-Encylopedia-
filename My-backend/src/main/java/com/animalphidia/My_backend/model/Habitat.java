package com.animalphidia.My_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "habitat")
public class Habitat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "habitat_id")
    private Integer habitatId;

    @Column(name = "habitat_name", nullable = false, length = 100)
    private String habitatName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String location;

    @Column(name = "climate_type", length = 50)
    private String climateType;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public Habitat() {}

    public Habitat(String habitatName) {
        this.habitatName = habitatName;
    }

    // Getters and Setters
    public Integer getHabitatId() { return habitatId; }
    public void setHabitatId(Integer habitatId) { this.habitatId = habitatId; }

    public String getHabitatName() { return habitatName; }
    public void setHabitatName(String habitatName) { this.habitatName = habitatName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getClimateType() { return climateType; }
    public void setClimateType(String climateType) { this.climateType = climateType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}