package com.example.backend.controller;

import com.example.backend.dto.Workspace.RawPictureRequest;
import com.example.backend.entity.Workspace.RawPicture;
import com.example.backend.repository.Workspace.RawPictureRepository;
import com.example.backend.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/workspace/RawPicture")
public class RawPictureHandler {

    @Autowired
    private RawPictureRepository rawPictureRepository;

    // 获取所有原始图片
    @GetMapping("/findAll")
    public List<RawPicture> findAllRawPictures() {
        return rawPictureRepository.findAll();
    }

    // 创建原始图片
    // 这里还不完全对，前端处理的应该是，整个图片，而不是url，
//    @PostMapping("/create")
//    public Response createRawPicture(@RequestBody RawPictureRequest rawPictureRequestDTO) {
//        RawPicture newRawPicture = new RawPicture();
//        newRawPicture.setWorkspaceId(rawPictureRequestDTO.getWorkspaceId());
//        newRawPicture.setImageUrl(rawPictureRequestDTO.getImageUrl());
//
//        rawPictureRepository.save(newRawPicture);
//
//        return new Response(200, "RawPicture created successfully");
//    }


    @PostMapping("/create")
    public ResponseEntity<Response> createRawPicture(@RequestParam("file") MultipartFile file, @RequestParam("workspaceId") Long workspaceId) {
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
        String uploadDir = "E:/111AIweb/backend/src/main/resources/static/rawpictures"; // 示例路径，根据实际情况修改
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs(); // 创建目录
        }

        // 生成唯一的文件名，避免文件名冲突
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = workspaceId + "_" + new Date().getTime() + fileExtension;
        String filePath = uploadDir + "/" + newFilename;

        // 保存文件到本地
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(500, "Failed to save file"));
        }

        // 构造文件的访问 URL
        String imageUrl = "http://localhost:8181/rawpictures/" + newFilename; // 示例 URL，根据实际情况修改

        // 存储图片信息到数据库
        RawPicture newRawPicture = new RawPicture();
        newRawPicture.setWorkspaceId(workspaceId);
        newRawPicture.setImageUrl(imageUrl);
        rawPictureRepository.save(newRawPicture);

        return ResponseEntity.ok(new Response(200, "RawPicture created successfully"));
    }


//    @PostMapping("/create")
//    public Response createRawPicture(@RequestBody RawPictureRequest rawPictureRequestDTO) {
//        RawPicture newRawPicture = new RawPicture();
//        newRawPicture.setWorkspaceId(rawPictureRequestDTO.getWorkspaceId());
//        // 保存 Base64 图片到文件系统或云存储
//        String imageUrl = saveImageToServer(rawPictureRequestDTO.getImageBase64());
//        newRawPicture.setImageUrl(imageUrl);
//        rawPictureRepository.save(newRawPicture);
//        return new Response(200, "RawPicture created successfully");
//    }
//
//    private String saveImageToServer(String base64Image) {
//        // 去掉 Base64 前缀
//        String base64ImageContent = base64Image.split(",")[1];
//        // 定义保存路径
//        String filePath = "uploads/" + System.currentTimeMillis() + ".png";
//        // 保存文件
//        try {
//            byte[] imageBytes = Base64.getDecoder().decode(base64ImageContent);
//            Files.write(Paths.get(filePath), imageBytes);
//            return filePath; // 返回服务器上的文件路径作为 URL
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }






    // 根据工作空间 ID 获取所有原始图片
    @GetMapping("/findByWorkspaceId/{workspaceId}")
    public List<RawPicture> getRawPicturesByWorkspaceId(@PathVariable Long workspaceId) {
        return rawPictureRepository.findByWorkspaceId(workspaceId);
    }

    // 根据 ID 更新原始图片配置
    @PostMapping("/configure/{pictureId}")
    public Response configureRawPicture(@PathVariable Long pictureId, @RequestBody RawPictureRequest rawPictureRequestDTO) {
        RawPicture existingRawPicture = rawPictureRepository.findByIdAndWorkspaceId(
                pictureId, rawPictureRequestDTO.getWorkspaceId());
        if (existingRawPicture == null) {
            return new Response(404, "RawPicture not found");
        }

        existingRawPicture.setImageUrl(rawPictureRequestDTO.getImageUrl());

        rawPictureRepository.save(existingRawPicture);

        return new Response(200, "RawPicture updated successfully");
    }

    // 删除原始图片
    @DeleteMapping("/delete/{pictureId}")
    public Response deleteRawPicture(@PathVariable Long pictureId, @RequestHeader("workspaceId") Long workspaceId) {
        RawPicture rawPicture = rawPictureRepository.findByWorkspaceIdAndId(workspaceId, pictureId);
        if (rawPicture == null) {
            return new Response(404, "RawPicture not found or unauthorized");
        }

        rawPictureRepository.deleteById(pictureId);

        return new Response(200, "RawPicture deleted successfully");
    }

    // 删除原始图片
//    @DeleteMapping("/delete/{pictureId}")
//    public Response deleteRawPicture(@PathVariable Long pictureId, @RequestBody RawPictureRequest rawPictureRequestDTO) {
//        System.out.println("Deleting picture with ID: " + pictureId + ", Workspace ID: " + rawPictureRequestDTO.getWorkspaceId());
//
//        RawPicture rawPicture = rawPictureRepository.findByIdAndWorkspaceId(
//                pictureId, rawPictureRequestDTO.getWorkspaceId());
//        if (rawPicture == null) {
//            return new Response(404, "RawPicture not found or unauthorized");
//        }
//
//        rawPictureRepository.deleteByIdAndWorkspaceId(pictureId, rawPictureRequestDTO.getWorkspaceId());
//
//        return new Response(200, "RawPicture deleted successfully");
//    }


}