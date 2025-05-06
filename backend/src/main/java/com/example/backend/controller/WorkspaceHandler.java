package com.example.backend.controller;

import com.example.backend.dto.Workspace.GeneratingPictureRequest;
import com.example.backend.dto.Workspace.PublishRequest;
import com.example.backend.dto.Workspace.WorkspaceRequest;
import com.example.backend.entity.Works.Works;
import com.example.backend.entity.Workspace.GeneratedPicture;
import com.example.backend.repository.Works.WorksRepository;
import com.example.backend.repository.Workspace.GeneratedPictureRepository;
import com.example.backend.repository.Workspace.WorkspaceRepository;
import com.example.backend.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entity.Workspace.Workspace;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workspace")
public class WorkspaceHandler {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private GeneratedPictureRepository generatedPictureRepository;

    @Autowired
    private WorksRepository worksRepository;

    @GetMapping("/findAll")
    public List<Workspace> findAllWorkspaces() {
        return workspaceRepository.findAll();
    }

    @PostMapping("/create")
    public Response createWorkspace(@RequestBody WorkspaceRequest workspaceRequestDTO) {
        // 检查工作空间名称是否已存在
        if (workspaceRepository.existsByNameAndUserId(workspaceRequestDTO.getName(), workspaceRequestDTO.getUserId())) {
            return new Response(400, "Workspace name already exists");
        }

        Workspace newWorkspace = new Workspace(); // 使用实体类 Workspace
        newWorkspace.setName(workspaceRequestDTO.getName());
        newWorkspace.setDescription(workspaceRequestDTO.getDescription());
        newWorkspace.setUserId(workspaceRequestDTO.getUserId());

        workspaceRepository.save(newWorkspace); // 保存实体类对象

        return new Response(200, "Workspace created successfully");
    }

    @PostMapping("/configure/{worksapceid}")
    public Response configureWorkspace(@PathVariable Long worksapceid, @RequestBody WorkspaceRequest workspaceRequestDTO, @RequestHeader("userId") Long userId) {
        Workspace existingWorkspace = workspaceRepository.findByUserIdAndId(userId, worksapceid); // 使用实体类 Workspace
        if (existingWorkspace == null) {
            return new Response(404, "Workspace not found");
        }

        existingWorkspace.setName(workspaceRequestDTO.getName());
        existingWorkspace.setDescription(workspaceRequestDTO.getDescription());

        workspaceRepository.save(existingWorkspace);

        return new Response(200, "Workspace updated successfully");
    }

    @GetMapping("/findByUserId")
    public List<Workspace> getWorkspacesByUserId(@RequestHeader("userId") Long userId) {
        //System.out.println("Received userId: " + userId);
        return workspaceRepository.findAllByUserId(userId);
    }

    // 删除工作空间
    @DeleteMapping("/delete/{worksapceid}")
    public Response deleteWorkspace(@PathVariable Long worksapceid, @RequestHeader("userId") Long userId) {
        // 检查工作空间是否存在且属于该用户
        Workspace workspace = workspaceRepository.findByUserIdAndId(userId, worksapceid);
        if (workspace == null) {
            return new Response(404, "Workspace not found or unauthorized");
        }

        workspaceRepository.deleteById(worksapceid); // 删除工作空间

        return new Response(200, "Workspace deleted successfully");
    }

    // 发布图片
    @PostMapping("/publish")
    public Response publishImage(@RequestBody PublishRequest publishRequest, @RequestHeader("userId") Long userId) {
        Long workspaceId = publishRequest.getWorkspaceId();
        String imageUrl = publishRequest.getImageUrl();
        String title = publishRequest.getTitle(); // 从请求体中获取标题
        String description = publishRequest.getDescription(); // 从请求体中获取描述


        // 创建一个新的 Works 实体并保存
        Works newWork = new Works();
        newWork.setUserId(userId);
        newWork.setTitle(title != null ? title : "Generated Image"); // 如果标题为 null，使用默认标题
        newWork.setDescription(description != null ? description : "Generated image from AI"); // 如果描述为 null，使用默认描述
        newWork.setImageUrl(imageUrl);
        newWork.setCreatedTime(LocalDateTime.now());
        newWork.setUpdatedTime(LocalDateTime.now());

        worksRepository.save(newWork);

        // 将新创建的 Works ID 返回给前端
        return new Response(200, "Image published successfully with Work ID: " + newWork.getWorkId());
    }

    @PostMapping("/generate-image/{workspaceId}")
    public Response generateImage(
            @PathVariable Long workspaceId,
            @RequestHeader("userId") Long userId,
            @RequestBody GeneratingPictureRequest generatingPictureRequest) {


        // 获取用户选择的两张图片和输入的文字
        String rawImageUrl = generatingPictureRequest.getRawImageUrl();
        String templateImageUrl = generatingPictureRequest.getTemplateImageUrl();
        String inputText = generatingPictureRequest.getInputText();

        if (rawImageUrl == null || templateImageUrl == null || inputText == null || inputText.isEmpty()) {
            return new Response(400, "Invalid request data");
        }

        // 调用算法组的接口生成图片
        try {
            String generatedImageUrl = "111";
            //String generatedImageUrl = callAlgorithmApi(rawImageUrl, templateImageUrl, inputText);

            // 创建一个新的 GeneratedPicture 实体并保存
            GeneratedPicture newGeneratedPicture = new GeneratedPicture();
            newGeneratedPicture.setWorkspaceId(workspaceId);
            newGeneratedPicture.setImageUrl(generatedImageUrl);
            newGeneratedPicture.setGenerationConfig(inputText); // 假设将输入文字作为生成配置

            generatedPictureRepository.save(newGeneratedPicture);

            // 返回生成的图片 URL 和 GeneratedPicture ID 给前端
            return new Response(200, "Image generated successfully with GeneratedPicture ID: " + newGeneratedPicture.getId(),
                    Map.of("imageUrl", generatedImageUrl, "generatedPictureId", newGeneratedPicture.getId()));
        } catch (Exception e) {
            return new Response(500, "Failed to generate image: " + e.getMessage());
        }
    }



}

