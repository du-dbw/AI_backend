package com.example.backend.controller;

import com.example.backend.dto.Response;
import com.example.backend.entity.Users.Follow.Follow;
import com.example.backend.entity.Users.Users;
import com.example.backend.repository.Users.Follow.FollowRepository;
import com.example.backend.repository.Users.UsersRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.example.backend.utils.TokenExtractInterceptor;


@RestController
@RequestMapping("/users")
public class FollowHandler {

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UsersRepository usersRepository;

    @PostMapping("/{username}/follow")
    public Response follow(HttpServletRequest request, @PathVariable String username) {

        // 从Cookie中提取Token
        String token = TokenExtractInterceptor.extractToken(request);

        if (token != null) {
            Users follower = usersRepository.findByToken(token);

            // 验证Token有效性及过期时间
            if (follower != null && follower.getTokenExpiry().isAfter(LocalDateTime.now())) {
                // 创建认证对象并指定权限
                Long followerId = follower.getId();

                Long followeeId = usersRepository.findByName(username).getId();

                // 检查关注关系是否已经存在
                if (followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
                    return new Response(400, "Already followed");
                }

                // 创建新的关注关系
                Follow newFollow = new Follow();
                newFollow.setFollowerId(followerId);
                newFollow.setFolloweeId(followeeId);
                newFollow.setCreatedAt(System.currentTimeMillis() + ""); // 使用当前时间作为字符串

                followRepository.save(newFollow);

                return new Response(0, "Follow successful");

            }
            else{
                return new Response(400, "Token timeout");
            }
        }
        else{
            return new Response(400, "Token not found");
        }


    }

    @DeleteMapping("/{username}/follow")
    @Transactional
    public Response unfollow(HttpServletRequest request, @PathVariable String username) {

        // 从Cookie中提取Token
        String token = TokenExtractInterceptor.extractToken(request);

        if (token != null) {
            Users follower = usersRepository.findByToken(token);

            // 验证Token有效性及过期时间
            if (follower != null && follower.getTokenExpiry().isAfter(LocalDateTime.now())) {
                // 创建认证对象并指定权限
                Long followerId = follower.getId();

                Long followeeId = usersRepository.findByName(username).getId();

                // 检查关注关系是否存在
                if (!followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
                    return new Response(400, "Not followed yet");
                }

                // 删除关注关系
                followRepository.deleteByFollowerIdAndFolloweeId(followerId, followeeId);

                return new Response(0, "Unfollow successful");

            }
            else{
                return new Response(400, "Token timeout");
            }
        }
        else{
            return new Response(400, "Token not found");
        }


    }

    @GetMapping("/{username}/followings")
    public Response getFollowings(
            @PathVariable String username,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize
    ) {
        // 查找用户
        Users user = usersRepository.findByName(username);
        if (user == null) {
            return new Response(404, "User not found");
        }

        // 计算分页参数
        int pageNum = page <= 0 ? 1 : page;
        int pageSizeNum = pageSize <= 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSizeNum);

        // 查询用户关注的作者列表
        Page<Follow> followingsPage = followRepository.findByFollowerId(user.getId(), pageable);

        // 将关注关系转换为用户信息
        List<Users> followings = followingsPage.stream()
                .map(follow -> usersRepository.findById(follow.getFolloweeId())
                        .orElseThrow(() -> new RuntimeException("Followee not found")))
                .collect(Collectors.toList());

        // 构建响应数据
        List<Map<String, String>> followingData = followings.stream().map(u -> {
            Map<String, String> data = new HashMap<>();
            data.put("username", u.getName());
            data.put("avatar_url", u.getAvatar());
            data.put("bio", u.getBio());
            return data;
        }).collect(Collectors.toList());

        return new Response(0, "Get followings successful", followingData);
    }

    @GetMapping("/{username}/followers")
    public Response getFollowers(
            @PathVariable String username,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize
    ) {
        // 查找用户
        Users user = usersRepository.findByName(username);
        if (user == null) {
            return new Response(404, "User not found");
        }

        // 计算分页参数
        int pageNum = page <= 0 ? 1 : page;
        int pageSizeNum = pageSize <= 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSizeNum);

        // 查询用户的粉丝列表
        Page<Follow> followersPage = followRepository.findByFolloweeId(user.getId(), pageable);

        // 将关注关系转换为用户信息
        List<Users> followers = followersPage.stream()
                .map(follow -> usersRepository.findById(follow.getFollowerId())
                        .orElseThrow(() -> new RuntimeException("Follower not found")))
                .collect(Collectors.toList());

        // 构建响应数据
        List<Map<String, String>> followerData = followers.stream().map(u -> {
            Map<String, String> data = new HashMap<>();
            data.put("username", u.getName());
            data.put("avatar_url", u.getAvatar());
            data.put("bio", u.getBio());
            return data;
        }).collect(Collectors.toList());

        return new Response(0, "Get followers successful", followerData);
    }




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