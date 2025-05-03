package com.example.backend.controller;

import com.example.backend.dto.Works.Comments.CommentsRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Works.Comments.Comments;
import com.example.backend.entity.Works.Works;
import com.example.backend.repository.Works.Comments.CommentsRepository;
import com.example.backend.repository.Works.WorksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/comments")
public class CommentsHandler {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private WorksRepository worksRepository;

    @PostMapping("/add")
    public Response addComment(@RequestBody CommentsRequest commentsRequest) {
        Long workID = commentsRequest.getWorkID();
        Long userID = commentsRequest.getUserID();
        String content = commentsRequest.getContent();


        // 创建新的评论
        Comments newComment = new Comments();
        newComment.setWorkId(workID);
        newComment.setUserId(userID);
        newComment.setContent(content);
        newComment.setCreatedTime(LocalDateTime.now()); // 使用当前时间

        commentsRepository.save(newComment);

        Works work = worksRepository.findById(workID).orElse(null);
        if (work != null) {
            work.addComment(); // 调用作品实体类中的方法增加评论数
            worksRepository.save(work); // 保存更新后的作品
        }

        return new Response(200, "Comment added successfully");
    }

    @PostMapping("/delete")
    public Response deleteComment(@RequestBody CommentsRequest commentsRequest) {
        Long workID = commentsRequest.getWorkID();
        Long userID = commentsRequest.getUserID();

        // 检查评论是否存在
        if (!commentsRepository.existsByWorkIdAndUserId(workID, userID)) {
            return new Response(400, "Comment does not exist");
        }

        // 删除评论
        commentsRepository.deleteByWorkIdAndUserId(workID, userID);

        Works work = worksRepository.findById(workID).orElse(null);
        if (work != null) {
            work.removeComment(); // 调用作品实体类中的方法减少评论数
            worksRepository.save(work); // 保存更新后的作品
        }
        return new Response(200, "Comment deleted successfully");
    }

    @GetMapping("/bywork/{workId}")
    public List<Comments> getCommentsByWork(@PathVariable Long workId) {
        return commentsRepository.findByWorkId(workId);
    }

    @GetMapping("/byuser/{userId}")
    public List<Comments> getCommentsByUser(@PathVariable Long userId) {
        return commentsRepository.findByUserId(userId);
    }
}