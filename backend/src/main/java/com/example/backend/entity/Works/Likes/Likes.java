package com.example.backend.entity.Works.Likes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likeId;

    @Column(nullable = false)
    private long workId;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private LocalDateTime createdTime;

    // 默认构造方法
    public Likes() {
    }

    // 全参构造方法
    public Likes(long workId, long userId, LocalDateTime createdTime) {
        this.workId = workId;
        this.userId = userId;
        this.createdTime = createdTime;
    }

    // Getter 和 Setter 方法
    public long getLikeId() {
        return likeId;
    }

    public void setLikeId(long likeId) {
        this.likeId = likeId;
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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "Likes{" +
                "likeId=" + likeId +
                ", workId=" + workId +
                ", userId=" + userId +
                ", createdTime=" + createdTime +
                "}\n";
    }
}