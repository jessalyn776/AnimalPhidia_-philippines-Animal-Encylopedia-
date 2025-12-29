package com.animalphidia.My_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "animal_media", indexes = {
        @Index(name = "idx_animal_id", columnList = "animal_id"),
        @Index(name = "idx_media_type", columnList = "media_type")
})
public class AnimalMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    // ✅ FIXED: Direct column mapping
    @Column(name = "animal_id", nullable = false)
    public Integer animalId;

    @NotBlank(message = "Media type is required")
    @Column(name = "media_type", nullable = false, length = 50)
    public String mediaType;

    @NotBlank(message = "Media URL is required")
    @Column(name = "media_url", nullable = false, columnDefinition = "LONGTEXT")
    public String mediaUrl;

    @Column(name = "caption", columnDefinition = "TEXT")
    public String caption;

    @Column(name = "credits", columnDefinition = "TEXT")
    public String credits;

    @Column(name = "display_order", nullable = false)
    public Integer displayOrder = 0;

    @Column(name = "featured", nullable = false)
    public Boolean featured = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    public AnimalMedia() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getAnimalId() { return animalId; }
    public void setAnimalId(Integer animalId) { this.animalId = animalId; }

    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public String getCredits() { return credits; }
    public void setCredits(String credits) { this.credits = credits; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public Boolean getFeatured() { return featured; }
    public void setFeatured(Boolean featured) { this.featured = featured; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}