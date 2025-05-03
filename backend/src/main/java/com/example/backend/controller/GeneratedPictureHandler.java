package com.example.backend.controller;

import com.example.backend.dto.Workspace.GeneratedPictureRequest;
import com.example.backend.entity.Workspace.GeneratedPicture;
import com.example.backend.repository.Workspace.GeneratedPictureRepository;
import com.example.backend.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspace/GeneratedPicture")
public class GeneratedPictureHandler {

    @Autowired
    private GeneratedPictureRepository generatedPictureRepository;

    // 获取所有生成的图片
    @GetMapping("/findAll")
    public List<GeneratedPicture> findAllGeneratedPictures() {
        return generatedPictureRepository.findAll();
    }

    // 创建生成的图片
    @PostMapping("/create")
    public Response createGeneratedPicture(@RequestBody GeneratedPictureRequest generatedPictureRequestDTO) {
        GeneratedPicture newGeneratedPicture = new GeneratedPicture();
        newGeneratedPicture.setWorkspaceId(generatedPictureRequestDTO.getWorkspaceId());
        newGeneratedPicture.setImageUrl(generatedPictureRequestDTO.getImageUrl());

        generatedPictureRepository.save(newGeneratedPicture);

        return new Response(200, "GeneratedPicture created successfully");
    }

    // 根据工作空间 ID 获取所有生成的图片
    @GetMapping("/findByWorkspaceId/{workspaceId}")
    public List<GeneratedPicture> getGeneratedPicturesByWorkspaceId(@PathVariable Long workspaceId) {
        return generatedPictureRepository.findByWorkspaceId(workspaceId);
    }

    // 根据 ID 更新生成的图片配置
    @PostMapping("/configure/{pictureId}")
    public Response configureGeneratedPicture(@PathVariable Long pictureId, @RequestBody GeneratedPictureRequest generatedPictureRequestDTO) {
        GeneratedPicture existingGeneratedPicture = generatedPictureRepository.findByIdAndWorkspaceId(
                pictureId, generatedPictureRequestDTO.getWorkspaceId());
        if (existingGeneratedPicture == null) {
            return new Response(404, "GeneratedPicture not found");
        }

        existingGeneratedPicture.setImageUrl(generatedPictureRequestDTO.getImageUrl());

        generatedPictureRepository.save(existingGeneratedPicture);

        return new Response(200, "GeneratedPicture updated successfully");
    }

    // 删除生成的图片
    @DeleteMapping("/delete/{pictureId}")
    public Response deleteGeneratedPicture(@PathVariable Long pictureId, @RequestHeader("workspaceId") Long workspaceId) {
        GeneratedPicture generatedPicture = generatedPictureRepository.findByWorkspaceIdAndId(workspaceId, pictureId);
        if (generatedPicture == null) {
            return new Response(404, "GeneratedPicture not found or unauthorized");
        }

        generatedPictureRepository.deleteById(pictureId);

        return new Response(200, "GeneratedPicture deleted successfully");
    }
}