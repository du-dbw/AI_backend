package com.example.backend.repository.Works.Comments;

import com.example.backend.entity.Works.Comments.Comments_Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

public interface Comments_CommentsRepository extends JpaRepository<Comments_Comments, Long> {

    @Query("SELECT cc FROM Comments_Comments cc WHERE cc.parentId = ?1 AND cc.userId = ?2")
    Comments_Comments findByParentIdAndUserId(Long parentId, Long userId);

    // 根据父评论和用户删除子评论
    @Modifying
    @Query("DELETE FROM Comments_Comments cc WHERE cc.parentId = ?1 AND cc.userId = ?2")
    @Transactional
    void deleteByParentIdAndUserId(Long parentId, Long userId);

    // 检查是否存在某个父评论下的子评论
    boolean existsByParentIdAndUserId(Long parentId, Long userId);

    // 获取某个父评论下的所有子评论
    List<Comments_Comments> findByParentId(Long parentId);

    // 获取某个用户的所有子评论（作为作者）
    List<Comments_Comments> findByUserId(Long userId);

    // 获取某个用户对某个父评论的子评论
    //List<Comments_Comments> findByParentIdAndUserId(Long parentId, Long userId);

    // 按时间筛选子评论
    List<Comments_Comments> findByCreatedTimeAfter(LocalDateTime date);
}