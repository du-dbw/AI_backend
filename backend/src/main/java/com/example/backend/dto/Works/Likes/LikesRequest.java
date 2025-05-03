package com.example.backend.dto.Works.Likes;

import java.time.LocalDateTime;

public class LikesRequest {
    private Long workId; // 点赞的作品
    private Long userId; // 点赞的用户
    private LocalDateTime createdTime; // 点赞的时间

    // Getter 和 Setter 方法
    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}