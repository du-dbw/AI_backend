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
public class Comments_Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likeId;

    @Column(nullable = false)
    private long commentId; // 被点赞的评论 ID

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private LocalDateTime likeTime;

    // 默认构造方法
    public Comments_Likes() {
    }

    // 全参构造方法
    public Comments_Likes(long commentId, long userId, LocalDateTime likeTime) {
        this.commentId = commentId;
        this.userId = userId;
        this.likeTime = likeTime;
    }

    // Getter 和 Setter 方法
    public long getLikeId() {
        return likeId;
    }

    public void setLikeId(long likeId) {
        this.likeId = likeId;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getLikeTime() {
        return likeTime;
    }

    public void setLikeTime(LocalDateTime likeTime) {
        this.likeTime = likeTime;
    }

    @Override
    public String toString() {
        return "Comments_Likes{" +
                "likeId=" + likeId +
                ", commentId=" + commentId +
                ", userId=" + userId +
                ", likeTime=" + likeTime +
                "}\n";
    }
}