package com.example.backend.controller;

import com.example.backend.dto.Works.WorksRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Users.Users;
import com.example.backend.entity.Works.Works;
import com.example.backend.repository.Favorites.FavoritesRepository;
import com.example.backend.repository.Users.UsersRepository;;
import com.example.backend.repository.Works.WorksRepository;
import com.example.backend.repository.Workspace.RawPictureRepository;
import com.example.backend.repository.Users.Follow.FollowRepository;
import com.example.backend.repository.Works.Likes.LikesRepository;
import com.example.backend.repository.Works.Comments.CommentsRepository;

import com.example.backend.repository.Workspace.GeneratedPictureRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import com.example.backend.entity.Workspace.RawPicture;
import com.example.backend.entity.Workspace.GeneratedPicture;
import jakarta.servlet.http.HttpServletRequest;
import com.example.backend.entity.Favorites.Favorites;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;


import com.example.backend.dto.Works.WorksCreatRequest;


@RestController
@RequestMapping("/works")
public class WorksHandler {

    @Autowired
    private WorksRepository worksRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private FavoritesRepository favoritesRepository;
    @Autowired
    private RawPictureRepository rawPictureRepository;
    @Autowired
    private GeneratedPictureRepository generatedPictureRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private CommentsRepository commentsRepository;




    @PostMapping("")
    public Response createWork(@RequestBody WorksCreatRequest worksCreatRequest , HttpServletRequest request) {

         //从请求中提取Token并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return new Response(401, "Unauthorized");
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return new Response(401, "Unauthorized");
        }

        //Users currentUser = usersRepository.findByName("11111");
        // 从请求中获取创建创作空间所需的参数
        Long userId = currentUser.getId();
        String title = worksCreatRequest.getName();
        String description = worksCreatRequest.getDescription();
        //String imageUrl = worksCreatRequest.getImageUrl();
        long likes = 0;
        long comments = 0;

        // 创建一个新的作品对象
        Works newWork = new Works();
        newWork.setUserId(userId);
        newWork.setTitle(title);
        newWork.setDescription(description);
        //newWork.setImageUrl(imageUrl);
        newWork.setLikes(likes);
        newWork.setComments(comments);
        newWork.setCreatedTime(LocalDateTime.now());
        newWork.setUpdatedTime(LocalDateTime.now()); // 确保设置更新时间
        newWork.setPublished(false); // 设置发布状态

        // 保存到数据库
        worksRepository.save(newWork);

        // 返回成功响应
        return new Response(0, "Work created successfully", newWork.getWorkId());
    }

    @GetMapping("")
    public Response getWorks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "all") String status,
            HttpServletRequest request
    ) {
        // 从请求中提取Token并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return new Response(401, "Unauthorized");
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return new Response(401, "Unauthorized");
        }

        //Users currentUser = usersRepository.findByName("11111");
        // 根据状态筛选作品
        List<Works> worksList;
        if ("published".equals(status)) {
            worksList = worksRepository.findByPublishedTrue(PageRequest.of(page - 1, pageSize));
        } else if ("draft".equals(status)) {
            worksList = worksRepository.findByPublishedFalse(PageRequest.of(page - 1, pageSize));
        } else {
            worksList = worksRepository.MYfindAll(PageRequest.of(page - 1, pageSize));
        }

        // 返回成功响应
        return new Response(0, "Works retrieved successfully", worksList);
    }

    @PostMapping("/{workid}/publish")
    public Response publishWork(@PathVariable Long workid, HttpServletRequest request) {
        // 从请求中提取Token并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return new Response(401, "Unauthorized");
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return new Response(401, "Unauthorized");
        }
        //Users currentUser = usersRepository.findByName("11111");
        // 获取 workid 对应的 Work 实体
        Works work = worksRepository.findById(workid)
                .orElseThrow(() -> new RuntimeException("Work not found"));

        // 检查 workid 所属用户是否与当前用户一致
        if (!work.getUserId().equals(currentUser.getId())) {
            return new Response(403, "You do not have permission to publish this work");
        }

        // 将作品标记为已发布
        work.setPublished(true);
        worksRepository.save(work);

        return new Response(0, "Work published successfully");
    }

    @GetMapping("/{workid}")
    public Response getWork(
            @PathVariable Long workid,
            HttpServletRequest request
    ) {
        // 从请求中提取Token并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return new Response(401, "Unauthorized");
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return new Response(401, "Unauthorized");
        }
        //Users currentUser = usersRepository.findByName("11111");
        // 获取 workid 对应的 Work 实体
        Works work = worksRepository.findById(workid)
                .orElseThrow(() -> new RuntimeException("Work not found"));

        // 检查 workid 所属用户是否与当前用户一致
        if (!work.getUserId().equals(currentUser.getId())) {
            return new Response(403, "You do not have permission to access this work");
        }

        // 获取原始图片和生成图片信息
        List<RawPicture> rawPictures = rawPictureRepository.findByWorkId(workid);
        List<GeneratedPicture> generatedPictures = generatedPictureRepository.findByWorkId(workid);

        // 构造响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("name", work.getTitle());
        data.put("description", work.getDescription());
        data.put("raw_pictures", rawPictures.stream().map(rp -> {
            Map<String, Object> rawPictureMap = new HashMap<>();
            rawPictureMap.put("fileid", rp.getId());
            return rawPictureMap;
        }).collect(Collectors.toList()));
        data.put("generated_pictures", generatedPictures.stream().map(gp -> {
            Map<String, Object> generatedPictureMap = new HashMap<>();
            generatedPictureMap.put("picture_id", gp.getId());
            return generatedPictureMap;
        }).collect(Collectors.toList()));

        return new Response(0, "Work retrieved successfully", data);
    }


    @GetMapping("/get-popular")
    public Response getPopularWorks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "likes_desc") String order,
            HttpServletRequest request
    ) {
        // 可以选择性地从请求中提取Token并验证用户身份，根据需求决定是否需要认证
        String token = extractToken(request);
        if (token == null) {
            return new Response(401, "Unauthorized");
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return new Response(401, "Unauthorized");
        }

        // 构造分页请求
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        // 获取热门作品列表
        List<Works> worksList;
        switch (order) {
            case "like_count":
                worksList = worksRepository.findAllByPublishedTrueOrderByLikesDesc(pageable);
                break;
            case "collection_count":
                worksList = worksRepository.findAllByPublishedTrueOrderByCollectionCountDesc(pageable);
                break;
            case "comment_count":
                worksList = worksRepository.findAllByPublishedTrueOrderByCommentsDesc(pageable);
                break;
            case "time":
            default:
                worksList = worksRepository.findAllByPublishedTrueOrderByCreatedTimeDesc(pageable);
                break;
        }

        // 返回成功响应
        return new Response(0, "Popular works retrieved successfully", worksList);
    }

    @GetMapping("/search")
    public Response searchWorksByTitle(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "time") String sort
    ) {

        // 构造分页请求
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        // 获取搜索结果列表
        List<Works> worksList;
        switch (sort) {
            case "like_count":
                worksList = worksRepository.searchByTitleContainingOrderByLikesDesc(keyword, pageable);
                break;
            case "collection_count":
                worksList = worksRepository.searchByTitleContainingOrderByCollectionCountDesc(keyword, pageable);
                break;
            case "comment_count":
                worksList = worksRepository.searchByTitleContainingOrderByCommentsDesc(keyword, pageable);
                break;
            case "time":
            default:
                worksList = worksRepository.searchByTitleContainingOrderByCreatedTimeDesc(keyword, pageable);
                break;
        }

        // 返回成功响应
        return new Response(0, "Search results retrieved successfully", worksList);
    }

    @GetMapping("/{workid}/view")
    public Response viewWork(
            @PathVariable Long workid
    ) {

        // 从数据库查询作品信息
        Works work = worksRepository.findById(workid)
                .orElseThrow(() -> new RuntimeException("Work not found"));

        // 构造返回的数据对象
        Map<String, Object> data = new HashMap<>();
        data.put("workid", work.getWorkId());
        data.put("name", work.getTitle());
        data.put("description", work.getDescription());

        Optional<Users> user = usersRepository.findById(work.getUserId());

        data.put("author", user.map(Users::getName).orElse("Unknown"));
        data.put("time", work.getCreatedTime());
        data.put("img", work.getImageUrl());
        data.put("like_count", work.getLikes());

        // 返回响应
        return new Response(0, "Work retrieved successfully", data);
    }

    @GetMapping("/{workid}/fullview")
    public Response fullViewWork(
            @PathVariable Long workid,
            HttpServletRequest request
    ) {

        // 从数据库查询作品信息
        Works work = worksRepository.findById(workid)
                .orElseThrow(() -> new RuntimeException("Work not found"));

        // 构造返回的数据对象
        Map<String, Object> data = new HashMap<>();

        // 作品基本信息
        data.put("workid", work.getWorkId());
        data.put("name", work.getTitle());
        data.put("description", work.getDescription());
        data.put("time", work.getCreatedTime().toEpochSecond(ZoneOffset.UTC)); // 转换为秒级时间戳
        data.put("like_count", work.getLikes());
        data.put("collection_count", work.getCollectionCount());

        // 作者信息
        Users author = usersRepository.findById(work.getUserId())
                .orElseThrow(() -> new RuntimeException("Author not found"));


        data.put("author", author.getName());
        data.put("avtar", author.getAvatar());

        // 获取当前用户信息（如果已登录）
        String token = extractToken(request);
        if (token == null) {
            return new Response(401, "Unauthorized");
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now()) || !Objects.equals(currentUser.getId(), author.getId())) {
            return new Response(401, "Unauthorized");
        }


        // 是否已关注
        boolean isFollow = false;
        isFollow = followRepository.existsByFollowerIdAndFolloweeId(currentUser.getId(), author.getId());

        data.put("isFollow", isFollow ? 1 : 0);

        // 是否已点赞
        boolean youLiked = false;
        youLiked = likesRepository.existsByWorkIdAndUserId(workid , currentUser.getId());

        data.put("you_liked", youLiked);

        // 是否已收藏
        boolean youCollected = false;
        youCollected = favoritesRepository.existsByWorkIDAndUserID(workid,currentUser.getId());

        data.put("you_collected", youCollected);

        // 作者是否允许查看原图
        //boolean allowRawPicture = author.isAllowRawPicture();
        boolean allowRawPicture = true;
        data.put("allow_raw_picture", allowRawPicture);

        // 图片信息
        List<Map<String, Object>> pictures = new ArrayList<>();

        // 获取原始图片
        List<RawPicture> rawPictures = rawPictureRepository.findByWorkId(workid);
        for (RawPicture rawPicture : rawPictures) {
            Map<String, Object> picture = new HashMap<>();
            picture.put("picture_url", rawPicture.getImageUrl());
            picture.put("picture_tag", "RAW");
            pictures.add(picture);
        }

        // 获取生成图片
        List<GeneratedPicture> generatedPictures = generatedPictureRepository.findByWorkId(workid);
        for (GeneratedPicture generatedPicture : generatedPictures) {
            Map<String, Object> picture = new HashMap<>();
            picture.put("picture_url", generatedPicture.getImageUrl());
            picture.put("picture_tag", "GENERATED");
            pictures.add(picture);
        }

        data.put("pictures", pictures);

        // 返回响应
        return new Response(0, "Work retrieved successfully", data);
    }

    // 获取相关作品推荐
    @GetMapping("/{workid}/related_works")
    public Response getRelatedWorks(@PathVariable Long workid) {
        // 调用findSimilarWorksByTitle方法来找到相关的作品，并按相似度排序
        List<Works> relatedWorks = worksRepository.findSimilarWorksByTitle(workid, 0); // 假设相似度阈值为0

        return new Response(0, "获取成功", relatedWorks);
    }


    // 新增收藏作品的 API
    @PostMapping("/{workId}/collect")
    public Response collectWork(@PathVariable Long workId, HttpServletRequest request) {
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

            // 检查是否已经收藏
            if (favoritesRepository.existsByWorkIDAndUserID(workId, currentUser.getId())) {
                return new Response(400, "Already collected");
            }

            // 创建收藏记录
            Favorites favorite = new Favorites();
            favorite.setWorkID(workId);
            favorite.setUserID(currentUser.getId());
            favorite.setCreatedTime(LocalDateTime.now());
            favoritesRepository.save(favorite);

            // 更新作品收藏数量
            work.addCollection();
            worksRepository.save(work);

            return new Response(0, "Collect successful");
        } catch (Exception e) {
            return new Response(500, "Collect failed");
        }
    }

    // 新增取消收藏作品的 API
    @DeleteMapping("/{workId}/collect")
    @Transactional
    public Response uncollectWork(@PathVariable Long workId, HttpServletRequest request) {
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

            // 检查是否收藏过
            if (!favoritesRepository.existsByWorkIDAndUserID(workId, currentUser.getId())) {
                return new Response(400, "Not collected");
            }

            // 删除收藏记录
            favoritesRepository.deleteByWorkIDAndUserID(workId, currentUser.getId());

            // 更新作品收藏数量
            work.removeCollection();
            worksRepository.save(work);

            return new Response(0, "Uncollect successful");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(500, "Uncollect failed");
        }
    }

    // 获取作品收藏状态的 API
    @GetMapping("/{workId}/collect")
    public Response getCollectStatus(@PathVariable Long workId, HttpServletRequest request) {
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

            // 获取收藏状态
            boolean isCollected = favoritesRepository.existsByWorkIDAndUserID(workId, currentUser.getId());
            long collectionCount = work.getCollectionCount();

            Map<String, Object> data = new HashMap<>();
            data.put("you_collected", isCollected);
            data.put("collection_count", collectionCount);

            return new Response(0, "Get collect status successful", data);
        } catch (Exception e) {
            return new Response(500, "Get collect status failed");
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