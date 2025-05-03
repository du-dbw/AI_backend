package com.example.backend.controller;

import com.example.backend.dto.Works.WorksRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Works.Works;
import com.example.backend.repository.Works.WorksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/works")
public class WorksHandler {

    @Autowired
    private WorksRepository worksRepository;

    @GetMapping("/findAll")
    public List<Works> findAll() {
        return worksRepository.findAll();
    }

    @PostMapping("/create")
    public Response createWork(@RequestBody WorksRequest worksRequest) {
        Long userId = worksRequest.getUserId();
        String title = worksRequest.getTitle();
        String description = worksRequest.getDescription();
        String imageUrl = worksRequest.getImageUrl();
        long likes = worksRequest.getLikes();
        long comments = worksRequest.getComments();
        LocalDateTime createdTime = worksRequest.getCreatedTime();

        Works newWork = new Works();
        newWork.setUserId(userId);
        newWork.setTitle(title);
        newWork.setDescription(description);
        newWork.setImageUrl(imageUrl);
        newWork.setLikes(likes);
        newWork.setComments(comments);
        newWork.setCreatedTime(createdTime);
        newWork.setUpdatedTime(LocalDateTime.now()); // Ensure updatedTime is set

        worksRepository.save(newWork);

        return new Response(200, "Work created successfully");
    }

    @PutMapping("/update/{workId}")
    public Response updateWork(@PathVariable Long workId, @RequestBody WorksRequest worksRequest) {
        Works work = worksRepository.findById(workId).orElse(null);
        if (work != null) {
            work.setTitle(worksRequest.getTitle());
            work.setDescription(worksRequest.getDescription());
            work.setImageUrl(worksRequest.getImageUrl());
            worksRepository.save(work);
            return new Response(200, "Work updated successfully");
        } else {
            return new Response(404, "Work not found");
        }
    }

    @DeleteMapping("/delete/{workId}")
    public Response deleteWork(@PathVariable Long workId) {
        Works work = worksRepository.findById(workId).orElse(null);
        if (work != null) {
            worksRepository.deleteById(workId);
            return new Response(200, "Work deleted successfully");
        } else {
            return new Response(404, "Work not found");
        }
    }

    // 按关注列表获取作品
    @GetMapping("/get-by-following")
    public Response getByFollowing(@RequestParam List<Long> followingUserIds, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 20); // 每页返回20个作品
        List<Works> worksList = worksRepository.findByUserIdIn(followingUserIds, pageable);
        return new Response(200, "Works retrieved successfully", worksList);
    }

    // 获取最新发布的作品
    @GetMapping("/get-latest")
    public Response getLatestWorks(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 20);
        List<Works> worksList = worksRepository.findAllByOrderByCreatedTimeDesc(pageable);
        return new Response(200, "Latest works retrieved successfully", worksList);
    }

    // 获取热度最高的作品
    @GetMapping("/get-popular")
    public Response getPopularWorks(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 20);
        List<Works> worksList = worksRepository.findAllByOrderByLikesDescCommentsDesc(pageable);
        return new Response(200, "Most popular works retrieved successfully", worksList);
    }

    // 综合排序获取作品
    @GetMapping("/get-comprehensive")
    public Response getComprehensiveWorks(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 20);
        List<Works> worksList = worksRepository.findAllByOrderByCreatedTimeDescLikesDescCommentsDesc(pageable);
        return new Response(200, "Comprehensively sorted works retrieved successfully", worksList);
    }
}