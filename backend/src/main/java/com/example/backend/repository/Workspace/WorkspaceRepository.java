package com.example.backend.repository.Workspace;

import com.example.backend.entity.Workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    Workspace findByUserIdAndId(Long userId, Long workspaceId);
    boolean existsByNameAndUserId(String name, Long userId);
}