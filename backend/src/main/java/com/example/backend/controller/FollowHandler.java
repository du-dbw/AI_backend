package com.example.backend.controller;

import com.example.backend.dto.Follow.FollowRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Follow.Follow;
import com.example.backend.repository.Follow.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
public class FollowHandler {

    @Autowired
    private FollowRepository followRepository;

    @PostMapping("/follow")
    public Response follow(@RequestBody FollowRequest followRequest) {
        Long followerId = followRequest.getFollowerId();
        Long followeeId = followRequest.getFolloweeId();

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

        return new Response(200, "Follow successful");
    }

    @PostMapping("/unfollow")
    public Response unfollow(@RequestBody FollowRequest followRequest) {

        Long followerId = followRequest.getFollowerId();
        Long followeeId = followRequest.getFolloweeId();


        // 检查关注关系是否存在
        if (!followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            return new Response(400, "Not followed");
        }

        // 删除关注关系
        followRepository.deleteByFollowerIdAndFolloweeId(followerId, followeeId);

        return new Response(200, "Unfollow successful");
    }

    @GetMapping("/following/{userId}")
    public List<Follow> getFollowing(@PathVariable Long userId) {
        return followRepository.findByFollowerId(userId);
    }

    @GetMapping("/followers/{userId}")
    public List<Follow> getFollowers(@PathVariable Long userId) {
        return followRepository.findByFolloweeId(userId);
    }
}