package com.animalphidia.My_backend.dto;

import java.time.LocalDateTime;

public class UserCollectionDTO {
    private Long id;
    private String collectionName;
    private String description;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private int count; // number of animals in collection

    public UserCollectionDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCollectionName() { return collectionName; }
    public void setCollectionName(String collectionName) { this.collectionName = collectionName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}