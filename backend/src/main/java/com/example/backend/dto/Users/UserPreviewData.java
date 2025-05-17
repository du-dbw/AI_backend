package com.example.backend.dto.Users;

import java.io.Serializable;

public class UserPreviewData implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号

    private Long followers;
    private String avatar;
    private boolean youFollowed;

    public UserPreviewData(Long followers, String avatar, boolean youFollowed) {
        this.followers = followers;
        this.avatar = avatar;
        this.youFollowed = youFollowed;
    }

    public Long getFollowers() {
        return followers;
    }

    public void setFollowers(Long followers) {
        this.followers = followers;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isYouFollowed() {
        return youFollowed;
    }

    public void setYouFollowed(boolean youFollowed) {
        this.youFollowed = youFollowed;
    }
}