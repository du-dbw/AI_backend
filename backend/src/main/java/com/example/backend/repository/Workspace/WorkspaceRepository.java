package com.example.backend.repository.Workspace;

import com.example.backend.entity.Workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    Workspace findByUserIdAndId(Long userId, Long workspaceId);
    boolean existsByNameAndUserId(String name, Long userId);
    // 根据用户 ID 返回所有的 workspace
    List<Workspace> findAllByUserId(Long userId);
}