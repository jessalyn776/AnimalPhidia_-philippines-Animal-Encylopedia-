package com.animalphidia.My_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "animals", indexes = {
        @Index(name = "idx_scientific_name", columnList = "scientific_name"),
        @Index(name = "idx_common_name", columnList = "common_name"),
        @Index(name = "idx_region", columnList = "region"),
        @Index(name = "idx_island", columnList = "island"),
        @Index(name = "idx_created_at", columnList = "created_at"),
        @Index(name = "idx_verified", columnList = "is_verified")
})
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    private Integer animalId;

    @NotBlank(message = "Common name is required")
    @Column(nullable = false, length = 255, name = "common_name")
    private String commonName;

    @Column(length = 150, name = "local_name")
    private String localName;

    @NotBlank(message = "Scientific name is required")
    @Column(nullable = false, length = 255, unique = true, name = "scientific_name")
    private String scientificName;

    // Foreign key relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id")
    private Species species;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kingdom_id")
    private Kingdom kingdom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phylum_id")
    private Phylum phylum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassTax classTax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private AnimalOrder animalOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genus_id")
    private Genus genus;

    // Foreign key id columns (direct mapping)
    @Column(name = "conservation_status_id")
    private Integer conservationStatusId;

    @Column(name = "diet_id")
    private Integer dietId;

    // String columns from database
    @Column(name = "conservation_status", length = 255)
    private String conservationStatusString;

    @Column(name = "diet", columnDefinition = "LONGTEXT")
    private String dietString;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @Column(columnDefinition = "TEXT", name = "behavior")
    private String behavior;

    @Column(columnDefinition = "TEXT", name = "reproduction")
    private String reproduction;

    @Column(length = 50, name = "lifespan")
    private String lifespan;

    @Column(length = 50, name = "size")
    private String size;

    @Column(length = 50, name = "weight")
    private String weight;

    @Column(length = 255, name = "region")
    private String region;

    @Column(length = 100, name = "province")
    private String province;

    @Column(length = 255, name = "island")
    private String island;

    @Column(name = "is_endangered")
    private Boolean isEndangered = false;

    @Column(name = "is_protected")
    private Boolean isProtected = false;

    @Column(name = "created_by")
    private Integer createdBy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "verified")
    private Boolean verified = false;

    @Column(columnDefinition = "TEXT", name = "verification_notes")
    private String verificationNotes;

    @Column(nullable = false, name = "active")
    private Boolean active = true;

    @Column(columnDefinition = "LONGTEXT", name = "characteristics")
    private String characteristics;

    @Column(columnDefinition = "LONGTEXT", name = "habitat")
    private String habitat;

    @Column(length = 255, name = "image_url")
    private String imageUrl;

    @Column(name = "population_estimate")
    private Double populationEstimate;

    @Column(columnDefinition = "TEXT", name = "tags")
    private String tags;

    @Column(name = "taxonomy_id")
    private Long taxonomyId;

    // Constructors
    public Animal() {}

    public Animal(String commonName, String scientificName) {
        this.commonName = commonName;
        this.scientificName = scientificName;
    }

    // Getters and Setters
    public Integer getAnimalId() { return animalId; }
    public void setAnimalId(Integer animalId) { this.animalId = animalId; }

    public String getCommonName() { return commonName; }
    public void setCommonName(String commonName) { this.commonName = commonName; }

    public String getLocalName() { return localName; }
    public void setLocalName(String localName) { this.localName = localName; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public Species getSpecies() { return species; }
    public void setSpecies(Species species) { this.species = species; }

    public Kingdom getKingdom() { return kingdom; }
    public void setKingdom(Kingdom kingdom) { this.kingdom = kingdom; }

    public Phylum getPhylum() { return phylum; }
    public void setPhylum(Phylum phylum) { this.phylum = phylum; }

    public ClassTax getClassTax() { return classTax; }
    public void setClassTax(ClassTax classTax) { this.classTax = classTax; }

    public AnimalOrder getAnimalOrder() { return animalOrder; }
    public void setAnimalOrder(AnimalOrder animalOrder) { this.animalOrder = animalOrder; }

    public Family getFamily() { return family; }
    public void setFamily(Family family) { this.family = family; }

    public Genus getGenus() { return genus; }
    public void setGenus(Genus genus) { this.genus = genus; }

    public Integer getConservationStatusId() { return conservationStatusId; }
    public void setConservationStatusId(Integer conservationStatusId) {
        this.conservationStatusId = conservationStatusId;
    }

    public Integer getDietId() { return dietId; }
    public void setDietId(Integer dietId) {
        this.dietId = dietId;
    }

    public String getConservationStatusString() { return conservationStatusString; }
    public void setConservationStatusString(String conservationStatusString) {
        this.conservationStatusString = conservationStatusString;
    }

    public String getDietString() { return dietString; }
    public void setDietString(String dietString) { this.dietString = dietString; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBehavior() { return behavior; }
    public void setBehavior(String behavior) { this.behavior = behavior; }

    public String getReproduction() { return reproduction; }
    public void setReproduction(String reproduction) { this.reproduction = reproduction; }

    public String getLifespan() { return lifespan; }
    public void setLifespan(String lifespan) { this.lifespan = lifespan; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getIsland() { return island; }
    public void setIsland(String island) { this.island = island; }

    public Boolean getIsEndangered() { return isEndangered; }
    public void setIsEndangered(Boolean isEndangered) { this.isEndangered = isEndangered; }

    public Boolean getIsProtected() { return isProtected; }
    public void setIsProtected(Boolean isProtected) { this.isProtected = isProtected; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }

    public String getVerificationNotes() { return verificationNotes; }
    public void setVerificationNotes(String verificationNotes) {
        this.verificationNotes = verificationNotes;
    }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public String getCharacteristics() { return characteristics; }
    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public String getHabitat() { return habitat; }
    public void setHabitat(String habitat) { this.habitat = habitat; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Double getPopulationEstimate() { return populationEstimate; }
    public void setPopulationEstimate(Double populationEstimate) {
        this.populationEstimate = populationEstimate;
    }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public Long getTaxonomyId() { return taxonomyId; }
    public void setTaxonomyId(Long taxonomyId) { this.taxonomyId = taxonomyId; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public Integer getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Integer updatedBy) { this.updatedBy = updatedBy; }
}