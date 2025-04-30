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
    public Response createWorkspace(@RequestBody WorkspaceRequest workspaceRequestDTO, @RequestHeader("userId") Long userId) {
        // 检查工作空间名称是否已存在
        if (workspaceRepository.existsByNameAndUserId(workspaceRequestDTO.getName(), userId)) {
            return new Response(400, "Workspace name already exists");
        }

        Workspace newWorkspace = new Workspace(); // 使用实体类 Workspace
        newWorkspace.setName(workspaceRequestDTO.getName());
        newWorkspace.setDescription(workspaceRequestDTO.getDescription());
        newWorkspace.setUserId(userId);

        workspaceRepository.save(newWorkspace); // 保存实体类对象

        return new Response(200, "Workspace created successfully");
    }

    @PostMapping("/configure/{workid}")
    public Response configureWorkspace(@PathVariable Long workid, @RequestBody WorkspaceRequest workspaceRequestDTO, @RequestHeader("userId") Long userId) {
        Workspace existingWorkspace = workspaceRepository.findByUserIdAndId(userId, workid); // 使用实体类 Workspace
        if (existingWorkspace == null) {
            return new Response(404, "Workspace not found");
        }

        existingWorkspace.setName(workspaceRequestDTO.getName());
        existingWorkspace.setDescription(workspaceRequestDTO.getDescription());

        workspaceRepository.save(existingWorkspace);

        return new Response(200, "Workspace updated successfully");
    }

    @GetMapping("/{workid}")
    public Workspace getWorkspace(@PathVariable Long workid, @RequestHeader("userId") Long userId) {
        Workspace workspace = workspaceRepository.findByUserIdAndId(userId, workid); // 使用实体类 Workspace
        if (workspace == null) {
            throw new RuntimeException("Workspace not found");
        }
        return workspace;
    }
}