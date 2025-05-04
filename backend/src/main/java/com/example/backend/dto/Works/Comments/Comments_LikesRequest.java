package com.example.backend.dto.Works.Comments;

import java.time.LocalDateTime;

public class Comments_LikesRequest {
    private Long commentId; // 被点赞的评论 ID
    private Long userId;    // 点赞的用户 ID
    private LocalDateTime likeTime; // 点赞时间

    // Getter 和 Setter 方法
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getLikeTime() {
        return likeTime;
    }

    public void setLikeTime(LocalDateTime likeTime) {
        this.likeTime = likeTime;
    }
}