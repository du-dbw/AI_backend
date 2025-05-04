package com.example.backend.controller;

import com.example.backend.dto.Works.Comments.Comments_LikesRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Works.Comments.Comments_Likes;
import com.example.backend.repository.Works.Comments.Comments_LikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/comments-likes")
public class Comments_LikesHandler {

    @Autowired
    private Comments_LikesRepository comments_LikesRepository;

    @PostMapping("/add")
    public Response addCommentLike(@RequestBody Comments_LikesRequest commentsLikesRequest) {
        Long commentId = commentsLikesRequest.getCommentId();
        Long userId = commentsLikesRequest.getUserId();

        // 检查是否已经点赞
        if (comments_LikesRepository.existsByCommentIdAndUserId(commentId, userId)) {
            return new Response(400, "You have already liked this comment");
        }

        // 创建新的点赞记录
        Comments_Likes newCommentLike = new Comments_Likes();
        newCommentLike.setCommentId(commentId);
        newCommentLike.setUserId(userId);
        newCommentLike.setLikeTime(LocalDateTime.now()); // 使用当前时间

        comments_LikesRepository.save(newCommentLike);

        return new Response(200, "Comment liked successfully");
    }

    @PostMapping("/delete")
    public Response deleteCommentLike(@RequestBody Comments_LikesRequest commentsLikesRequest) {
        Long commentId = commentsLikesRequest.getCommentId();
        Long userId = commentsLikesRequest.getUserId();

        // 检查点赞记录是否存在
        if (!comments_LikesRepository.existsByCommentIdAndUserId(commentId, userId)) {
            return new Response(400, "You have not liked this comment");
        }

        // 删除点赞记录
        comments_LikesRepository.deleteByCommentIdAndUserId(commentId, userId);

        return new Response(200, "Comment like removed successfully");
    }

    @GetMapping("/bycomment/{commentId}")
    public List<Comments_Likes> getLikesByComment(@PathVariable Long commentId) {
        return comments_LikesRepository.findByCommentId(commentId);
    }

    @GetMapping("/byuser/{userId}")
    public List<Comments_Likes> getLikesByUser(@PathVariable Long userId) {
        return comments_LikesRepository.findByUserId(userId);
    }
}