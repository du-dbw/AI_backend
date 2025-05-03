package com.example.backend.dto.Comments;

import java.time.LocalDateTime;

public class CommentsRequest {
    private Long workID; // 评论所属的作品
    private Long userID; // 评论的作者
    private String content; // 评论的内容
    private LocalDateTime createdTime; // 评论的创建时间

    // Getter 和 Setter 方法
    public Long getWorkID() {
        return workID;
    }

    public void setWorkID(Long workID) {
        this.workID = workID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}