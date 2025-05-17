package com.example.backend.dto.Workspace;

public class RawPictureRequest {
    private Long id;
    private Long workId;
    private String imageUrl;

    // 默认构造函数
    public RawPictureRequest() {}

    // 全参构造函数
    public RawPictureRequest(Long id, Long workId, String imageUrl) {
        this.id = id;
        this.workId = workId;
        this.imageUrl = imageUrl;
    }

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

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}