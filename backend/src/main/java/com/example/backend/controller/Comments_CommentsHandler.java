package com.example.backend.controller;

import com.example.backend.dto.Works.Comments.Comments_CommentsRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Works.Comments.Comments_Comments;
import com.example.backend.repository.Works.Comments.Comments_CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/comments-comments")
public class Comments_CommentsHandler {

    @Autowired
    private Comments_CommentsRepository comments_CommentsRepository;

    @PostMapping("/add")
    public Response addCommentComment(@RequestBody Comments_CommentsRequest commentsCommentsRequest) {
        Long parentId = commentsCommentsRequest.getParentId();
        Long userID = commentsCommentsRequest.getUserID();
        String content = commentsCommentsRequest.getContent();

        // 创建新的子评论
        Comments_Comments newCommentComment = new Comments_Comments();
        newCommentComment.setParentId(parentId);
        newCommentComment.setUserId(userID);
        newCommentComment.setContent(content);
        newCommentComment.setCreatedTime(LocalDateTime.now()); // 使用当前时间

        comments_CommentsRepository.save(newCommentComment);

        return new Response(200, "Sub-comment added successfully");
    }

    @PostMapping("/delete")
    public Response deleteCommentComment(@RequestBody Comments_CommentsRequest commentsCommentsRequest) {
        Long parentId = commentsCommentsRequest.getParentId();
        Long userID = commentsCommentsRequest.getUserID();

        // 检查子评论是否存在
        if (!comments_CommentsRepository.existsByParentIdAndUserId(parentId, userID)) {
            return new Response(400, "Sub-comment does not exist");
        }

        // 删除子评论
        comments_CommentsRepository.deleteByParentIdAndUserId(parentId, userID);

        return new Response(200, "Sub-comment deleted successfully");
    }

    @GetMapping("/byparent/{parentId}")
    public List<Comments_Comments> getCommentCommentsByParent(@PathVariable Long parentId) {
        return comments_CommentsRepository.findByParentId(parentId);
    }

    @GetMapping("/byuser/{userId}")
    public List<Comments_Comments> getCommentCommentsByUser(@PathVariable Long userId) {
        return comments_CommentsRepository.findByUserId(userId);
    }
}