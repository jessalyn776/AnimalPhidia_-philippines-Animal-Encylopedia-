package com.animalphidia.My_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "species")
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "species_id")
    private Integer speciesId;

    @Column(name = "species_name", nullable = false, length = 150)
    private String speciesName;

    @Column(name = "genus_id")
    private Integer genusId;

    @Column(name = "scientific_name", length = 200)
    private String scientificName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // REMOVED: Invalid relationship since Animal only has speciesId, not species object
    // @OneToMany(mappedBy = "species", fetch = FetchType.LAZY)
    // private Set<Animal> animals = new HashSet<>();

    // Getters and Setters
    public Integer getSpeciesId() { return speciesId; }
    public void setSpeciesId(Integer speciesId) { this.speciesId = speciesId; }

    public String getSpeciesName() { return speciesName; }
    public void setSpeciesName(String speciesName) { this.speciesName = speciesName; }

    public Integer getGenusId() { return genusId; }
    public void setGenusId(Integer genusId) { this.genusId = genusId; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}