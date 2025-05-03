package com.example.backend.dto.Workspace;

public class RawPictureRequest {
    private Long id;
    private Long workspaceId;
    private String imageUrl;

    // 默认构造函数
    public RawPictureRequest() {}

    // 全参构造函数
    public RawPictureRequest(Long id, Long workspaceId, String imageUrl) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}