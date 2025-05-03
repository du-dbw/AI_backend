package com.example.backend.entity.Works;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Works {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workId;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 65535)
    private String description;

    @Column(length = 255)
    private String imageUrl;

    // 添加点赞数和评论数字段，并初始化为0
    @Column(nullable = false)
    private long likes = 0;

    @Column(nullable = false)
    private long comments = 0;

    @Column(nullable = false)
    private LocalDateTime createdTime;

    @Column
    private LocalDateTime updatedTime;

    // 默认构造方法
    public Works() {
    }

    // 全参构造方法
    public Works(Long userId, String title, String description, String imageUrl) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
        this.likes = 0; // 初始化点赞数
        this.comments = 0; // 初始化评论数
    }

    // 为点赞数增加1
    public long addLike() {
        this.likes += 1;
        return this.likes;
    }

    // 为点赞数减少1
    public long removeLike() {
        if (this.likes > 0) {
            this.likes -= 1;
        }
        return this.likes;
    }

    // 为评论数增加1
    public long addComment() {
        this.comments += 1;
        return this.comments;
    }

    // 为评论数减少1
    public long removeComment() {
        if (this.comments > 0) {
            this.comments -= 1;
        }
        return this.comments;
    }

    // Getter 和 Setter 方法（如果使用Lombok的@Data注解，这些方法可以自动生成）

    // 以下是手动编写的getter/setter方法示例（如果需要）
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toString() {
        return "Works{" +
                "workId=" + workId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", likes=" + likes +
                ", comments=" + comments +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                "}\n";
    }
}