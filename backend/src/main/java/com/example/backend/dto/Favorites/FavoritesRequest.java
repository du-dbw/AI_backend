package com.example.backend.dto.Favorites;

import java.time.LocalDateTime;

public class FavoritesRequest {

    private Long favoriteID;
    private Long workID;
    private Long userID;
    private LocalDateTime createdTime;

    // Getters and Setters
    public Long getFavoriteID() {
        return favoriteID;
    }

    public void setFavoriteID(Long favoriteID) {
        this.favoriteID = favoriteID;
    }

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

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}