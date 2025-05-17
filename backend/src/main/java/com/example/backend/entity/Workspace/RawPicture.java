package com.example.backend.entity.Workspace;

import jakarta.persistence.*;

@Entity
//@Table(name = "raw_picture")
public class RawPicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long workId;
    private String imageUrl;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}