package com.example.backend.controller;

import com.example.backend.dto.Workspace.WorkspaceRequest;
import com.example.backend.repository.Workspace.WorkspaceRepository;
import com.example.backend.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entity.Workspace.Workspace;


import java.util.List;

@RestController
@RequestMapping("/workspace")
public class WorkspaceHandler {

    @Autowired
    private WorkspaceRepository workspaceRepository;

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



}

