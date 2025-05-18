package com.example.backend.controller;


import com.example.backend.dto.Response;
import com.example.backend.entity.Works.Likes.Likes;
import com.example.backend.entity.Works.Works;
import com.example.backend.repository.Users.UsersRepository;
import com.example.backend.repository.Works.Likes.LikesRepository;
import com.example.backend.repository.Works.WorksRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import com.example.backend.entity.Users.Users;
import com.example.backend.utils.TokenExtractInterceptor;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import java.util.*;


@RestController
@RequestMapping("/works")
public class LikesHandler {

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private WorksRepository worksRepository;

    @Autowired
    private UsersRepository usersRepository;

    // 点赞作品的 API
    @PostMapping("/{workId}/like")
    public Response likeWork(@PathVariable Long workId, HttpServletRequest request) {
        // 从请求中提取Token并验证用户身份
        String token = TokenExtractInterceptor.extractToken(request);
        if (token == null) {
            return new Response(401, "Unauthorized");
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return new Response(401, "Unauthorized");
        }
        //Users currentUser = usersRepository.findByName("11111");
        try {
            // 检查作品是否存在
            Works work = worksRepository.findById(workId).orElseThrow(() -> new RuntimeException("Work not found"));

            // 检查是否已经点过赞
            if (likesRepository.existsByWorkIdAndUserId(workId, currentUser.getId())) {
                return new Response(400, "Already liked");
            }

            // 创建点赞记录
            Likes like = new Likes();
            like.setWorkId(workId);
            like.setUserId(currentUser.getId());
            like.setCreatedTime(LocalDateTime.now());
            likesRepository.save(like);

            // 更新作品点赞数量
            work.addLike();
            worksRepository.save(work);

            return new Response(0, "Like successful");
        } catch (Exception e) {
            return new Response(500, "Like failed");
        }
    }

    // 取消点赞作品的 API
    @DeleteMapping("/{workId}/like")
    @Transactional
    public Response unlikeWork(@PathVariable Long workId, HttpServletRequest request) {
        // 从请求中提取Token并验证用户身份
        String token = TokenExtractInterceptor.extractToken(request);
        if (token == null) {
            return new Response(401, "Unauthorized");
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return new Response(401, "Unauthorized");
        }
        //Users currentUser = usersRepository.findByName("11111");
        try {
            // 检查作品是否存在
            Works work = worksRepository.findById(workId).orElseThrow(() -> new RuntimeException("Work not found"));

            // 检查是否点过赞
            if (!likesRepository.existsByWorkIdAndUserId(workId, currentUser.getId())) {
                return new Response(400, "Not liked");
            }

            // 删除点赞记录
            likesRepository.deleteByWorkIdAndUserId(workId, currentUser.getId());

            // 更新作品点赞数量
            work.removeLike();
            worksRepository.save(work);

            return new Response(0, "Unlike successful");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(500, "Unlike failed");
        }
    }

    // 获取作品点赞状态的 API
    @GetMapping("/{workId}/like")
    public Response getLikeStatus(@PathVariable Long workId, HttpServletRequest request) {
        // 从请求中提取Token并验证用户身份
        String token = TokenExtractInterceptor.extractToken(request);
        if (token == null) {
            return new Response(401, "Unauthorized");
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return new Response(401, "Unauthorized");
        }
        //Users currentUser = usersRepository.findByName("11111");
        try {
            // 检查作品是否存在
            Works work = worksRepository.findById(workId).orElseThrow(() -> new RuntimeException("Work not found"));

            // 获取点赞状态
            boolean youLiked = likesRepository.existsByWorkIdAndUserId(workId, currentUser.getId());
            long likeCount = work.getLikes();

            Map<String, Object> data = new HashMap<>();
            data.put("you_liked", youLiked);
            data.put("like_count", likeCount);

            return new Response(0, "Get like status successful", data);
        } catch (Exception e) {
            return new Response(500, "Get like status failed");
        }
    }

    // 提取 Token 的方法
//    private String extractToken(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("userToken".equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }


}