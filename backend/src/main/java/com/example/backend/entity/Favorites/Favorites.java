package com.example.backend.entity.Favorites;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
//@Table(name = "favorites")
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteID;

    @Column(nullable = false)
    private Long workID;

    @Column(nullable = false)
    private Long userID;

    @Column( nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdTime;

    // 默认构造方法
    public Favorites() {
    }

    // 全参构造方法
    public Favorites(Long workID, Long userID) {
        this.workID = workID;
        this.userID = userID;
    }

    // Getter 和 Setter 方法
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



    @Override
    public String toString() {
        return "Favorites{" +
                "favoriteID=" + favoriteID +
                ", workID=" + workID +
                ", userID=" + userID +
                ", createdTime=" + createdTime +
                "}\n";
    }


}