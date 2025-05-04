package com.example.backend.dto.Works.Comments;

import java.time.LocalDateTime;

public class Comments_CommentsRequest {
    private Long parentId; // 父评论的 ID
    private Long userID;   // 子评论的作者
    private String content; // 子评论的内容
    private LocalDateTime createdTime; // 子评论的创建时间

    // Getter 和 Setter 方法
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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