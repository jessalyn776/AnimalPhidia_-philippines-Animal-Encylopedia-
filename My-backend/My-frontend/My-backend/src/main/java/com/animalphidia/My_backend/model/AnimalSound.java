
package com.animalphidia.My_backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "animal_sounds")
public class AnimalSound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sound_id")
    private Integer soundId;

    @Column(name = "animal_id", nullable = false)
    private Integer animalId;

    @Column(name = "sound_url", nullable = false, length = 255)
    private String soundUrl;

    @Column(name = "sound_type", length = 50)
    private String soundType;

    @Column(length = 255)
    private String description;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "uploaded_by")
    private Integer uploadedBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", insertable = false, updatable = false)
    private Animal animal;

    // Constructors
    public AnimalSound() {}

    public AnimalSound(Integer animalId, String soundUrl) {
        this.animalId = animalId;
        this.soundUrl = soundUrl;
    }

    // Getters and Setters
    public Integer getSoundId() { return soundId; }
    public void setSoundId(Integer soundId) { this.soundId = soundId; }

    public Integer getAnimalId() { return animalId; }
    public void setAnimalId(Integer animalId) { this.animalId = animalId; }

    public String getSoundUrl() { return soundUrl; }
    public void setSoundUrl(String soundUrl) { this.soundUrl = soundUrl; }

    public String getSoundType() { return soundType; }
    public void setSoundType(String soundType) { this.soundType = soundType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }

    public Integer getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(Integer uploadedBy) { this.uploadedBy = uploadedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }
}