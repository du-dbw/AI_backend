package com.example.backend.controller;

import com.example.backend.dto.Response;
import com.example.backend.entity.Works.Comments.Comments;
import com.example.backend.entity.Users.Users;
import com.example.backend.entity.Works.Works;
import com.example.backend.repository.Users.UsersRepository;
import com.example.backend.repository.Works.Comments.CommentsRepository;
import com.example.backend.repository.Works.WorksRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/works")
public class CommentsHandler {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private WorksRepository worksRepository;

    @Autowired
    private UsersRepository usersRepository;

    // 评论作品的 API
    @PostMapping("/{workId}/comments")
    public Response commentWork(@PathVariable Long workId, @RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        // 从请求中提取Token并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return new Response(401, "Unauthorized");
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return new Response(401, "Unauthorized");
        }

        try {
            // 检查作品是否存在
            Works work = worksRepository.findById(workId).orElseThrow(() -> new RuntimeException("Work not found"));

            // 获取评论内容
            String content = requestBody.get("content");
            if (content == null || content.isEmpty()) {
                return new Response(400, "Comment content is required");
            }

            // 创建评论记录
            Comments comment = new Comments();
            comment.setWorkId(workId);
            comment.setUserId(currentUser.getId());
            comment.setContent(content);
            comment.setCreatedTime(LocalDateTime.now());
            commentsRepository.save(comment);

            // 更新作品评论数量
            work.addComment();
            worksRepository.save(work);

            return new Response(0, "Comment successful");
        } catch (Exception e) {
            return new Response(500, "Comment failed");
        }
    }

    // 获取评论列表的 API
    @GetMapping("/{workId}/comments")
    public Response getComments(@PathVariable Long workId,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int page_size,
                                HttpServletRequest request) {
        try {
            // 检查作品是否存在
            worksRepository.findById(workId).orElseThrow(() -> new RuntimeException("Work not found"));

            // 构造分页请求，按时间倒序排列
            Pageable pageable = PageRequest.of(page - 1, page_size, Sort.by(Sort.Direction.DESC, "createdTime"));
            // 分页获取评论列表
            Page<Comments> commentPage = commentsRepository.findByWorkId(workId, pageable);

            // 将评论转换为数据列表
            List<Map<String, Object>> comments = commentPage.getContent().stream().map(comment -> {
                Map<String, Object> commentData = new HashMap<>();
                commentData.put("comment_id", comment.getCommentId());
                commentData.put("content", comment.getContent());
                commentData.put("username", usersRepository.findById(comment.getUserId()).map(Users::getName).orElse("Unknown"));
                commentData.put("time", comment.getCreatedTime().toEpochSecond(java.time.ZoneOffset.UTC));
                return commentData;
            }).collect(Collectors.toList());

            return new Response(0, "Get comments successful", comments);
        } catch (Exception e) {
            return new Response(500, "Get comments failed");
        }
    }

    // 提取 Token 的方法
    private String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}