package com.example.backend.controller;

import com.example.backend.dto.Works.Likes.LikesRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Works.Likes.Likes;
import com.example.backend.entity.Works.Works;
import com.example.backend.repository.Works.Likes.LikesRepository;
import com.example.backend.repository.Works.WorksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/likes")
public class LikesHandler {

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private WorksRepository worksRepository;

    @PostMapping("/add")
    public Response addLike(@RequestBody LikesRequest likesRequest) {
        Long workId = likesRequest.getWorkId();
        Long userId = likesRequest.getUserId();

        // 检查是否已经点过赞
        if (likesRepository.existsByWorkIdAndUserId(workId, userId)) {
            return new Response(400, "Already liked");
        }

        // 创建新的点赞记录
        Likes newLike = new Likes();
        newLike.setWorkId(workId);
        newLike.setUserId(userId);
        newLike.setCreatedTime(LocalDateTime.now()); // 使用当前时间

        likesRepository.save(newLike);

        // 更新作品的点赞数
        Works work = worksRepository.findById(workId).orElse(null);
        if (work != null) {
            work.addLike(); // 调用作品实体类中的方法增加点赞数
            worksRepository.save(work); // 保存更新后的作品
        }

        return new Response(200, "Like added successfully");
    }

    @PostMapping("/delete")
    public Response deleteLike(@RequestBody LikesRequest likesRequest) {
        Long workId = likesRequest.getWorkId();
        Long userId = likesRequest.getUserId();

        // 检查点赞记录是否存在
        if (!likesRepository.existsByWorkIdAndUserId(workId, userId)) {
            return new Response(400, "Like does not exist");
        }

        // 删除点赞记录
        likesRepository.deleteByWorkIdAndUserId(workId, userId);

        Works work = worksRepository.findById(workId).orElse(null);
        if (work != null) {
            work.removeLike(); // 调用作品实体类中的方法减少点赞数
            worksRepository.save(work); // 保存更新后的作品
        }
        return new Response(200, "Like deleted successfully");
    }

    @GetMapping("/bywork/{workId}")
    public List<Likes> getLikesByWork(@PathVariable Long workId) {
        return likesRepository.findByWorkId(workId);
    }

    @GetMapping("/byuser/{userId}")
    public List<Likes> getLikesByUser(@PathVariable Long userId) {
        return likesRepository.findByUserId(userId);
    }
}