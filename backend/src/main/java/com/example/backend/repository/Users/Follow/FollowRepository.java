package com.example.backend.repository.Users.Follow;

import com.example.backend.entity.Users.Follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 根据关注者和被关注者查找关注关系
    Follow findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    // 根据关注者和被关注者删除关注关系（修复点）
    @Modifying
    @Query("DELETE FROM Follow f WHERE f.followerId = ?1 AND f.followeeId = ?2")
    @Transactional  // 确保事务在 Repository 层生效（或在 Service/Controller 层添加）
    void deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    // 检查关注关系是否存在
    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    // 获取某个用户的所有关注者
    List<Follow> findByFolloweeId(Long followeeId);

    // 获取某个用户的所有被关注者
    List<Follow> findByFollowerId(Long followerId);
}