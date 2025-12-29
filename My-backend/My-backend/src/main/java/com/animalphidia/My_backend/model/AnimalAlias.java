package com.animalphidia.My_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "animal_aliases", indexes = {
        @Index(name = "idx_animal_id", columnList = "animal_id"),
        @Index(name = "idx_alias_name", columnList = "alias_name")
})
public class AnimalAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    // ✅ FIXED: Use direct column mapping (not relationship)
    @Column(name = "animal_id", nullable = false)
    public Integer animalId;

    @NotBlank(message = "Alias name is required")
    @Column(nullable = false, length = 255)
    public String aliasName;

    @Column(length = 100)
    public String language;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    public LocalDateTime createdAt;

    public AnimalAlias() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getAnimalId() { return animalId; }
    public void setAnimalId(Integer animalId) { this.animalId = animalId; }

    public String getAliasName() { return aliasName; }
    public void setAliasName(String aliasName) { this.aliasName = aliasName; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}