package com.example.backend.dto.Workspace;

public class GeneratedPictureRequest {
    private Long id; // 新增的 id 字段
    private String imageUrl;
    private String generationConfig;
    private Long workspaceId;

    // 无参构造函数
    public GeneratedPictureRequest() {
    }

    // 全参构造函数
    public GeneratedPictureRequest(Long id, String imageUrl, String generationConfig, Long workspaceId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.generationConfig = generationConfig;
        this.workspaceId = workspaceId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }
}