package com.example.backend.repository.Favorites;

import com.example.backend.entity.Favorites.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    // 根据 workID 和 userID 查找收藏记录
    Favorites findByWorkIDAndUserID(Long workID, Long userID);

    // 检查某个用户是否已经收藏了某个作品
    boolean existsByWorkIDAndUserID(Long workID, Long userID);

    // 根据用户 ID 返回所有的收藏记录
    List<Favorites> findAllByUserID(Long userID);

    // 根据作品 ID 返回所有的收藏记录
    List<Favorites> findAllByWorkID(Long workID);

    // 根据用户 ID 删除所有的收藏记录
    void deleteAllByUserID(Long userID);

    // 根据作品 ID 删除所有的收藏记录
    void deleteAllByWorkID(Long workID);

    // 添加收藏（通过插入一条新的收藏记录）
    @Modifying
    @Query("INSERT INTO Favorites(workID, userID, createdTime) VALUES(?1, ?2, CURRENT_TIMESTAMP)")
    void addFavorite(Long workID, Long userID);

    // 取消收藏（通过删除指定的收藏记录）
    @Modifying
    @Query("DELETE FROM Favorites WHERE workID = ?1 AND userID = ?2")
    void removeFavorite(Long workID, Long userID);
}