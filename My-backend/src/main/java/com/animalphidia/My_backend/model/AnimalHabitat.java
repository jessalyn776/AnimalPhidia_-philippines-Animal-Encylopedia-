package com.animalphidia.My_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "animal_habitat")
public class AnimalHabitat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_habitat_id")
    private Integer animalHabitatId;

    @Column(name = "animal_id", nullable = false)
    private Integer animalId;

    @Column(name = "habitat_id", nullable = false)
    private Integer habitatId;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", insertable = false, updatable = false)
    private Animal animal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitat_id", insertable = false, updatable = false)
    private Habitat habitat;

    // Constructors
    public AnimalHabitat() {}

    public AnimalHabitat(Integer animalId, Integer habitatId) {
        this.animalId = animalId;
        this.habitatId = habitatId;
    }

    // Getters and Setters
    public Integer getAnimalHabitatId() { return animalHabitatId; }
    public void setAnimalHabitatId(Integer animalHabitatId) { this.animalHabitatId = animalHabitatId; }

    public Integer getAnimalId() { return animalId; }
    public void setAnimalId(Integer animalId) { this.animalId = animalId; }

    public Integer getHabitatId() { return habitatId; }
    public void setHabitatId(Integer habitatId) { this.habitatId = habitatId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }

    public Habitat getHabitat() { return habitat; }
    public void setHabitat(Habitat habitat) { this.habitat = habitat; }
}