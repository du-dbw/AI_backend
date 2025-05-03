package com.example.backend.controller;

import com.example.backend.dto.Workspace.RawPictureRequest;
import com.example.backend.entity.Workspace.RawPicture;
import com.example.backend.repository.Workspace.RawPictureRepository;
import com.example.backend.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/create")
    public Response createRawPicture(@RequestBody RawPictureRequest rawPictureRequestDTO) {
        RawPicture newRawPicture = new RawPicture();
        newRawPicture.setWorkspaceId(rawPictureRequestDTO.getWorkspaceId());
        newRawPicture.setImageUrl(rawPictureRequestDTO.getImageUrl());

        rawPictureRepository.save(newRawPicture);

        return new Response(200, "RawPicture created successfully");
    }

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