package com.example.backend.repository.Users.Follow;

import com.example.backend.entity.Users.Follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 根据关注者和被关注者查找关注关系
    Follow findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    // 根据关注者和被关注者删除关注关系（修复点）
    @Modifying
    @Query("DELETE FROM Follow f WHERE f.followerId = ?1 AND f.followeeId = ?2")
    @Transactional  // 确保事务在 Repository 层生效（或在 Service/Controller 层添加）
    void deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeName);

    // 检查关注关系是否存在
    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    // 获取某个用户的所有关注者
    List<Follow> findByFolloweeId(Long followeeId);

    // 获取某个用户的所有被关注者
    List<Follow> findByFollowerId(Long followerId);

    // 获取某个用户的所有被关注者（支持分页）
    Page<Follow> findByFollowerId(Long followerId, Pageable pageable);

    // 获取某个用户的所有粉丝（支持分页）
    Page<Follow> findByFolloweeId(Long followeeId, Pageable pageable);

    // 统计用户关注数
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followerId = ?1")
    long countByFollowerId(Long userId);

    // 统计用户被关注数
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followeeId = ?1")
    long countByFolloweeId(Long userId);
}