package com.animalphidia.My_backend.dto;

import java.time.LocalDateTime;

public class SpeciesDTO {
    public Integer speciesId;
    public String speciesName;
    public String commonName;
    public String scientificName;
    public String descriptionText;
    public String characteristics;
    public String habitat;

    // Conservation info
    public Integer conservationStatusId;
    public String conservationStatus;

    // Taxonomy IDs
    public Integer kingdomId;
    public Integer phylumId;
    public Integer classId;
    public Integer orderId;
    public Integer familyId;
    public Integer genusId;

    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    // Constructors
    public SpeciesDTO() {}

    public SpeciesDTO(Integer speciesId, String speciesName, String commonName,
                      String scientificName, Integer conservationStatusId) {
        this.speciesId = speciesId;
        this.speciesName = speciesName;
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.conservationStatusId = conservationStatusId;
    }

    // Getters and Setters
    public Integer getSpeciesId() { return speciesId; }
    public void setSpeciesId(Integer speciesId) { this.speciesId = speciesId; }

    public String getSpeciesName() { return speciesName; }
    public void setSpeciesName(String speciesName) { this.speciesName = speciesName; }

    public String getCommonName() { return commonName; }
    public void setCommonName(String commonName) { this.commonName = commonName; }

    public String getScientificName() { return scientificName; }
    public void setScientificName(String scientificName) { this.scientificName = scientificName; }

    public String getDescriptionText() { return descriptionText; }
    public void setDescriptionText(String descriptionText) { this.descriptionText = descriptionText; }

    public String getCharacteristics() { return characteristics; }
    public void setCharacteristics(String characteristics) { this.characteristics = characteristics; }

    public String getHabitat() { return habitat; }
    public void setHabitat(String habitat) { this.habitat = habitat; }

    public Integer getConservationStatusId() { return conservationStatusId; }
    public void setConservationStatusId(Integer conservationStatusId) {
        this.conservationStatusId = conservationStatusId;
    }

    public String getConservationStatus() { return conservationStatus; }
    public void setConservationStatus(String conservationStatus) { this.conservationStatus = conservationStatus; }

    public Integer getKingdomId() { return kingdomId; }
    public void setKingdomId(Integer kingdomId) { this.kingdomId = kingdomId; }

    public Integer getPhylumId() { return phylumId; }
    public void setPhylumId(Integer phylumId) { this.phylumId = phylumId; }

    public Integer getClassId() { return classId; }
    public void setClassId(Integer classId) { this.classId = classId; }

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getFamilyId() { return familyId; }
    public void setFamilyId(Integer familyId) { this.familyId = familyId; }

    public Integer getGenusId() { return genusId; }
    public void setGenusId(Integer genusId) { this.genusId = genusId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}