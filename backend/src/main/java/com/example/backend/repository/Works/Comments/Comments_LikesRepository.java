package com.example.backend.repository.Works.Comments;

import com.example.backend.entity.Works.Comments.Comments_Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

public interface Comments_LikesRepository extends JpaRepository<Comments_Likes, Long> {

    @Query("SELECT cl FROM Comments_Likes cl WHERE cl.commentId = ?1 AND cl.userId = ?2")
    Comments_Likes findByCommentIdAndUserId(Long commentId, Long userId);

    // 根据评论 ID 和用户 ID 删除点赞记录
    @Modifying
    @Query("DELETE FROM Comments_Likes cl WHERE cl.commentId = ?1 AND cl.userId = ?2")
    @Transactional
    void deleteByCommentIdAndUserId(Long commentId, Long userId);

    // 检查是否已点赞
    boolean existsByCommentIdAndUserId(Long commentId, Long userId);

    // 获取某个评论的所有点赞记录
    List<Comments_Likes> findByCommentId(Long commentId);

    // 获取某个用户的所有点赞记录
    List<Comments_Likes> findByUserId(Long userId);

    // 获取某个用户对某个评论的点赞记录
    //List<Comments_Likes> findByCommentIdAndUserId(Long commentId, Long userId);

    // 按点赞时间筛选
    List<Comments_Likes> findByLikeTimeAfter(LocalDateTime date);
}