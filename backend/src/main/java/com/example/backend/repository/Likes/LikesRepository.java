package com.example.backend.repository.Likes;

import com.example.backend.entity.Likes.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query("SELECT l FROM Likes l WHERE l.workId = ?1 AND l.userId = ?2")
    Likes findByWorkIdAndUserId(Long workId, Long userId);

    // 根据作品和用户删除点赞记录
    @Modifying
    @Query("DELETE FROM Likes l WHERE l.workId = ?1 AND l.userId = ?2")
    @Transactional
    void deleteByWorkIdAndUserId(Long workId, Long userId);

    // 检查点赞记录是否存在
    boolean existsByWorkIdAndUserId(Long workId, Long userId);

    // 获取某个作品的所有点赞记录
    List<Likes> findByWorkId(Long workId);

    // 获取某个用户的全部点赞记录
    List<Likes> findByUserId(Long userId);

    // 按时间筛选点赞记录
    List<Likes> findByCreatedTimeAfter(LocalDateTime date);
}