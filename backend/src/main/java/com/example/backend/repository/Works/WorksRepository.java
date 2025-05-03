package com.example.backend.repository.Works;

import com.example.backend.entity.Works.Works;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorksRepository extends JpaRepository<Works, Long> {
    // 根据作品 ID 查找作品
    Works findByWorkId(Long workId);

    // 根据用户 ID 查找该用户的所有作品
    List<Works> findByUserId(Long userId);

    // 根据作品 ID 删除作品（修复点）
    @Modifying
    @Query("DELETE FROM Works w WHERE w.workId = ?1")
    @Transactional
    void deleteByWorkId(Long workId);

    // 检查作品是否存在
    boolean existsByWorkId(Long workId);


    // 根据用户关注列表获取作品（假设关注列表是用户ID的集合）
    List<Works> findByUserIdIn(List<Long> followingUserIds, Pageable pageable);

    // 获取最新发布的作品（按createdTime降序排序）
    List<Works> findAllByOrderByCreatedTimeDesc(Pageable pageable);

    // 获取热度最高的作品（按likes和comments降序排序）
    List<Works> findAllByOrderByLikesDescCommentsDesc(Pageable pageable);

    // 综合排序（可以根据需要调整排序逻辑）
    List<Works> findAllByOrderByCreatedTimeDescLikesDescCommentsDesc(Pageable pageable);



}