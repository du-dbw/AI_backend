package com.example.backend.controller;

import com.example.backend.dto.Workspace.GeneratedPictureRequest;
import com.example.backend.entity.Workspace.GeneratedPicture;
import com.example.backend.repository.Users.UsersRepository;
import com.example.backend.repository.Works.Tags.TagsRepository;
import com.example.backend.entity.Users.Users;
import com.example.backend.repository.Works.WorksRepository;
import com.example.backend.repository.Workspace.GeneratedPictureRepository;
import com.example.backend.dto.Response;
import com.example.backend.repository.Workspace.RawPictureRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entity.Works.Works;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/works")
public class GeneratedPictureHandler {

    @Autowired
    private GeneratedPictureRepository generatedPictureRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private WorksRepository worksRepository;


    @GetMapping("/{workid}/generated_picture/{picture_id}")
    public Response getGeneratedPicture(
            @PathVariable Long workid,
            @PathVariable Long picture_id,
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work not found"));

        // 检查 workid 所属用户是否与当前用户一致
        if (!work.getUserId().equals(currentUser.getId())) {
            return new Response(403, "You do not have permission to access this work");
        }

        // 获取指定的 GeneratedPicture 实体
        GeneratedPicture generatedPicture = generatedPictureRepository.findById(picture_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "GeneratedPicture not found"));

        // 构造响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("picture_url", generatedPicture.getImageUrl());

        return new Response(0, "GeneratedPicture retrieved successfully", data);
    }


    @DeleteMapping("/{workid}/generated_picture/{picture_id}")
    @Transactional
    public Response deleteGeneratedPicture(
            @PathVariable Long workid,
            @PathVariable Long picture_id,
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

        // 获取 workid 对应的 Work 实体
        Works work = worksRepository.findById(workid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work not found"));

        // 检查 workid 所属用户是否与当前用户一致
        if (!work.getUserId().equals(currentUser.getId())) {
            return new Response(403, "You do not have permission to delete this work's generated picture");
        }

        // 获取指定的 GeneratedPicture 实体
        GeneratedPicture generatedPicture = generatedPictureRepository.findById(picture_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "GeneratedPicture not found"));

        // 检查该图片是否属于该作品
        if (!generatedPicture.getWorkId().equals(workid)) {
            return new Response(400, "This generated picture does not belong to the specified work");
        }

        // 删除图片
        generatedPictureRepository.delete(generatedPicture);

        return new Response(0, "GeneratedPicture deleted successfully");
    }





    // 获取所有生成的图片
//    @GetMapping("/findAll")
//    public List<GeneratedPicture> findAllGeneratedPictures() {
//        return generatedPictureRepository.findAll();
//    }
//
//    // 创建生成的图片
//    @PostMapping("/create")
//    public Response createGeneratedPicture(@RequestBody GeneratedPictureRequest generatedPictureRequestDTO) {
//        GeneratedPicture newGeneratedPicture = new GeneratedPicture();
//        newGeneratedPicture.setWorkId(generatedPictureRequestDTO.getWorkspaceId());
//        newGeneratedPicture.setImageUrl(generatedPictureRequestDTO.getImageUrl());
//


//        generatedPictureRepository.save(newGeneratedPicture);
//
//        return new Response(200, "GeneratedPicture created successfully");
//    }
//
//    // 根据工作空间 ID 获取所有生成的图片
//    @GetMapping("/findByWorkspaceId/{workspaceId}")
//    public List<GeneratedPicture> getGeneratedPicturesByWorkspaceId(@PathVariable Long workspaceId) {
//        return generatedPictureRepository.findByWorkId(workspaceId);
//    }
//
//    // 根据 ID 更新生成的图片配置
//    @PostMapping("/configure/{pictureId}")
//    public Response configureGeneratedPicture(@PathVariable Long pictureId, @RequestBody GeneratedPictureRequest generatedPictureRequestDTO) {
//        GeneratedPicture existingGeneratedPicture = generatedPictureRepository.findByIdAndWorkId(
//                pictureId, generatedPictureRequestDTO.getWorkspaceId());
//        if (existingGeneratedPicture == null) {
//            return new Response(404, "GeneratedPicture not found");
//        }
//
//        existingGeneratedPicture.setImageUrl(generatedPictureRequestDTO.getImageUrl());
//
//        generatedPictureRepository.save(existingGeneratedPicture);
//
//        return new Response(200, "GeneratedPicture updated successfully");
//    }
//
//    // 删除生成的图片
//    @DeleteMapping("/delete/{pictureId}")
//    public Response deleteGeneratedPicture(@PathVariable Long pictureId, @RequestHeader("workspaceId") Long workspaceId) {
//        GeneratedPicture generatedPicture = generatedPictureRepository.findByWorkIdAndId(workspaceId, pictureId);
//        if (generatedPicture == null) {
//            return new Response(404, "GeneratedPicture not found or unauthorized");
//        }
//
//        generatedPictureRepository.deleteById(pictureId);
//
//        return new Response(200, "GeneratedPicture deleted successfully");
//    }

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