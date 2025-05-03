package com.example.backend.dto.Workspace;

public class PublishRequest {
    private String imageUrl; // 图片的 URL 或文件路径
    private Long workspaceId; // 工作空间 ID
    private String title; // 标题
    private String description; // 描述

    // Getter 和 Setter 方法
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}