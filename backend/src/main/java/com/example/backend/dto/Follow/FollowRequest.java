package com.example.backend.dto.Follow;

public class FollowRequest {
    private Long followerId; // 关注人的 ID
    private Long followeeId; // 被关注人的 ID

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFolloweeId() {
        return followeeId;
    }

    public void setFolloweeId(Long followeeId) {
        this.followeeId = followeeId;
    }
}