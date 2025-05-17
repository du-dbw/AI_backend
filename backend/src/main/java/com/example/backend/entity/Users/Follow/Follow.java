package com.example.backend.entity.Users.Follow;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Data;

@Entity
@Data
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private Long followerId; // 关注者 ID

    @Column(nullable = false)
    private Long followeeId; // 被关注者 ID

    @Column(nullable = false)
    private String createdAt; // 关注时间

    // 默认构造方法
    public Follow() {
    }

    // 全参构造方法
    public Follow(Long followerId, Long followeeId, String createdAt) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.createdAt = createdAt;
    }

    // Getter 和 Setter 方法
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", followerId=" + followerId +
                ", followeeId=" + followeeId +
                ", createdAt='" + createdAt + '\'' +
                "}\n";
    }
}