package com.example.backend.controller;

import com.example.backend.entity.Users.Users;
import com.example.backend.entity.Works.Tags.Tags;
import com.example.backend.entity.Works.Works;
import com.example.backend.entity.Workspace.GeneratedPicture;
import com.example.backend.entity.Workspace.RawPicture;
import com.example.backend.repository.Users.UsersRepository;
import com.example.backend.repository.Works.WorksRepository;
import com.example.backend.repository.Workspace.GeneratedPictureRepository;
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
import com.example.backend.utils.TokenExtractInterceptor;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/works")
public class GeneratePictureHandler {

    @Autowired
    private RawPictureRepository rawPictureRepository;
    @Autowired
    private GeneratedPictureRepository generatedPictureRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private WorksRepository worksRepository;

    @GetMapping("/{workid}/generated_picture/{picture_id}")
    public ResponseEntity<Response> getRawPicture(
            @PathVariable Long workid,
            @PathVariable Long picture_id,
            HttpServletRequest request
    ) {
        // 从请求中提取Token并验证用户身份
        String token = TokenExtractInterceptor.extractToken(request);
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

        // 获取指定的 RawPicture 实体
        GeneratedPicture genPicture = generatedPictureRepository.findById(picture_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "genPicture not found"));

        // 构造响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("picture_url", genPicture.getImageUrl());

        return ResponseEntity.ok(new Response(0, "genPicture retrieved successfully", data));
    }

    @DeleteMapping("/{workid}/generated_picture/{picture_id}")
    @Transactional
    public ResponseEntity<Response> deleteRawPicture(
            @PathVariable Long workid,
            @PathVariable Long picture_id,
            HttpServletRequest request
    ) {
        // 从请求中提取Token并验证用户身份
        String token = TokenExtractInterceptor.extractToken(request);
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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(403, "You do not have permission to delete from this work"));
        }

        // 获取指定的 RawPicture 实体
        GeneratedPicture genPicture = generatedPictureRepository.findById(picture_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "genPicture not found"));

        // 删除文件（如果存在）
        String filePath = genPicture.getImageUrl().replace("E:/111AIweb/backend/src/main/resources/static/rawpictures", "E:/111AIweb/backend/src/main/resources/static/rawpictures");
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(500, "Failed to delete file"));
            }
        }

        // 删除数据库记录
        generatedPictureRepository.delete(genPicture);

        return ResponseEntity.ok(new Response(0, "RawPicture deleted successfully", null));
    }


}