package com.example.backend.entity.Workspace;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class GeneratedPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long workId;
    private String imageUrl;
    private String generationConfig;
    private Long userId;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workspaceId) {
        this.workId = workspaceId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGenerationConfig() {
        return generationConfig;
    }

    public void setGenerationConfig(String generationConfig) {
        this.generationConfig = generationConfig;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}