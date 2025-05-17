package com.example.backend.controller;

import com.example.backend.entity.Users.Users;
import com.example.backend.entity.Works.Tags.Tags;
import com.example.backend.entity.Works.Works;
import com.example.backend.entity.Workspace.RawPicture;
import com.example.backend.repository.Users.UsersRepository;
import com.example.backend.repository.Works.WorksRepository;
import com.example.backend.repository.Workspace.RawPictureRepository;
import com.example.backend.dto.Response;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.example.backend.repository.Works.Tags.TagsRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/works")
public class RawPictureHandler {

    @Autowired
    private RawPictureRepository rawPictureRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private WorksRepository worksRepository;
    @Autowired
    private TagsRepository tagsRepository;

    private final String uploadDir = "E:/111AIweb/backend/src/main/resources/static/rawpictures";



    // 获取所有原始图片
    @GetMapping("/findAll")
    public List<RawPicture> findAllRawPictures() {
        return rawPictureRepository.findAll();
    }




    @PostMapping("/{workid}/raw_picture")
    public ResponseEntity<Response> uploadRawPicture(
            @PathVariable Long workid,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {

        // 从请求中提取Token并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(401, "Unauthorized"));
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(401, "Unauthorized"));
        }

        // 获取 workid 对应的 Work 实体
        Works work = worksRepository.findById(workid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work not found"));

        // 检查 workid 所属用户是否与当前用户一致
        if (!work.getUserId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(403, "You do not have permission to upload to this work"));
        }

        // 检查文件是否为空
        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400, "File is empty"));
        }

        // 检查文件类型是否为图片
        String contentType = file.getContentType();
        if (!(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400, "File type not supported"));
        }

        // 创建文件保存目录（如果不存在）
        String uploadDir = "E:/111AIweb/backend/src/main/resources/static/rawpictures";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 生成唯一的文件名，避免文件名冲突
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = workid + "_" + new Date().getTime() + fileExtension;
        String filePath = uploadDir + "/" + newFilename;

        // 保存文件到本地
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(500, "Failed to save file"));
        }

        // 构造文件的访问 URL
        String imageUrl = "http://localhost:8181/rawpictures/" + newFilename;

        // 存储图片信息到数据库
        RawPicture newRawPicture = new RawPicture();
        newRawPicture.setWorkId(workid);
        newRawPicture.setUserId(currentUser.getId());
        newRawPicture.setImageUrl(imageUrl);
        rawPictureRepository.save(newRawPicture);

        return ResponseEntity.ok(new Response(0, "RawPicture created successfully", newRawPicture.getId()));
    }

    @GetMapping("/{workid}/raw_picture/{picture_id}")
    public ResponseEntity<Response> getRawPicture(
            @PathVariable Long workid,
            @PathVariable Long picture_id,
            HttpServletRequest request
    ) {
        // 从请求中提取Token并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(401, "Unauthorized"));
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(401, "Unauthorized"));
        }

        //Users currentUser = usersRepository.findByName("11111");
        // 获取 workid 对应的 Work 实体
        Works work = worksRepository.findById(workid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work not found"));

        // 检查 workid 所属用户是否与当前用户一致
        if (!work.getUserId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(403, "You do not have permission to access this work"));
        }

        // 获取指定的 RawPicture 实体
        RawPicture rawPicture = rawPictureRepository.findById(picture_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RawPicture not found"));

        // 构造响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("file_url", rawPicture.getImageUrl());

        return ResponseEntity.ok(new Response(0, "RawPicture retrieved successfully", data));
    }

    @DeleteMapping("/{workid}/raw_picture/{picture_id}")
    @Transactional
    public ResponseEntity<Response> deleteRawPicture(
            @PathVariable Long workid,
            @PathVariable Long picture_id,
            HttpServletRequest request
    ) {
        // 从请求中提取Token并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(401, "Unauthorized"));
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(401, "Unauthorized"));
        }
        //Users currentUser = usersRepository.findByName("11111");
        // 获取 workid 对应的 Work 实体
        Works work = worksRepository.findById(workid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work not found"));

        // 检查 workid 所属用户是否与当前用户一致
        if (!work.getUserId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(403, "You do not have permission to delete from this work"));
        }

        // 获取指定的 RawPicture 实体
        RawPicture rawPicture = rawPictureRepository.findById(picture_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RawPicture not found"));

        // 删除文件（如果存在）
        String filePath = rawPicture.getImageUrl().replace("E:/111AIweb/backend/src/main/resources/static/rawpictures", uploadDir + "/");
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(500, "Failed to delete file"));
            }
        }

        // 删除数据库记录
        rawPictureRepository.delete(rawPicture);

        return ResponseEntity.ok(new Response(0, "RawPicture deleted successfully", null));
    }

    // 配置创作空间 - POST
    @PostMapping("/{workid}/configure")
    public ResponseEntity<Response> configureWork(
            @PathVariable Long workid,
            @RequestBody Map<String, Object> requestBody,
            HttpServletRequest request
    ) {
        // 从请求中提取Token并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(401, "Unauthorized"));
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(401, "Unauthorized"));
        }

        // 获取 workid 对应的 Work 实体
        Works work = worksRepository.findById(workid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work not found"));

        // 检查 workid 所属用户是否与当前用户一致
        if (!work.getUserId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(403, "You do not have permission to configure this work"));
        }

        // 解析请求体中的数据
        String name = (String) requestBody.get("name");
        String description = (String) requestBody.get("description");
        List<String> tags = (List<String>) requestBody.get("tags");

        // 验证请求参数是否为空
        if (name == null || name.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400, "Name is required"));
        }

        // 更新 Work 实体信息
        work.setTitle(name);
        work.setDescription(description);
        worksRepository.save(work);

        // 处理标签数据
        if (tags != null && !tags.isEmpty()) {
            // 对标签名称进行去重处理
            Set<String> uniqueTags = new HashSet<>(tags);
            tags = new ArrayList<>(uniqueTags);

            // 先删除旧标签
            tagsRepository.deleteByWorkId(workid);

            // 添加新标签
            for (String tagName : tags) {
                if (tagName != null && !tagName.isEmpty()) {
                    Tags tag = new Tags();
                    tag.setName(tagName.trim()); // 去除多余的空格
                    tag.setWorkId(workid);
                    tagsRepository.save(tag);
                }
            }
        }

        return ResponseEntity.ok(new Response(0, "Work configured successfully"));
    }

    // 获取创作空间配置 - GET
    @GetMapping("/{workid}/configure")
    public ResponseEntity<Response> getWorkConfiguration(
            @PathVariable Long workid,
            HttpServletRequest request
    ) {
        // 从请求中提取Token并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(401, "Unauthorized"));
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(401, "Unauthorized"));
        }

        // 获取 workid 对应的 Work 实体
        Works work = worksRepository.findById(workid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work not found"));

        // 检查 workid 所属用户是否与当前用户一致
        if (!work.getUserId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(403, "You do not have permission to access this work"));
        }

        // 获取对应作品的所有标签
        List<Tags> tagList = tagsRepository.findByWorkId(workid);
        List<String> tags = tagList.stream().map(Tags::getName).collect(Collectors.toList());

        // 构造响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("name", work.getTitle());
        data.put("description", work.getDescription());
        data.put("tags", tags);

        return ResponseEntity.ok(new Response(0, "Work configuration retrieved successfully", data));
    }





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