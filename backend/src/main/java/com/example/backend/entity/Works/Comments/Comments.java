package com.example.backend.entity.Works.Comments;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    @Column(nullable = false)
    private long workId;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdTime;

    // 默认构造方法
    public Comments() {
    }

    // 全参构造方法
    public Comments(long workId, long userId, String content, LocalDateTime createdTime) {
        this.workId = workId;
        this.userId = userId;
        this.content = content;
        this.createdTime = createdTime;
    }

    // Getter 和 Setter 方法
    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getWorkId() {
        return workId;
    }

    public void setWorkId(long workId) {
        this.workId = workId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return "Comments{" +
                "commentId=" + commentId +
                ", workId=" + workId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", createdTime=" + createdTime +
                "}\n";
    }
}