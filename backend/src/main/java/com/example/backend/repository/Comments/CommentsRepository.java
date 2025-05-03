package com.example.backend.repository.Comments;

import com.example.backend.entity.Comments.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

public interface CommentsRepository extends JpaRepository<Comments, Long> {

    @Query("SELECT c FROM Comments c WHERE c.workId = ?1 AND c.userId = ?2")
    Comments findByWorkIdAndUserId(Long workId, Long userId);

    // 根据作品和作者删除评论（修复点）
    @Modifying
    @Query("DELETE FROM Comments c WHERE c.workId = ?1 AND c.userId = ?2")
    @Transactional  // 确保事务在 Repository 层生效（或在 Service/Controller 层添加）
    void deleteByWorkIdAndUserId(Long workId, Long userId);

    // 检查评论是否存在
    boolean existsByWorkIdAndUserId(Long workId, Long userId);

    // 获取某个作品的所有评论
    List<Comments> findByWorkId(Long workId);

    // 获取某个用户的全部评论（作为作者）
    List<Comments> findByUserId(Long userId);

    // 按时间筛选评论
    // 这里暂不支持，按热度吧，不过也可以实现
    List<Comments> findByCreatedTimeAfter(LocalDateTime date);
}