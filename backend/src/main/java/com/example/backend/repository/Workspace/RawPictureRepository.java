package com.example.backend.repository.Workspace;

import com.example.backend.entity.Workspace.RawPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RawPictureRepository extends JpaRepository<RawPicture, Long> {
    List<RawPicture> findByWorkspaceId(String workspaceId);
    RawPicture findByIdAndWorkspaceId(Long id, String workspaceId);
    void deleteByIdAndWorkspaceId(Long id, String workspaceId);
}