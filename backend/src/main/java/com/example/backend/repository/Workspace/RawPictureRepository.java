package com.example.backend.repository.Workspace;

import com.example.backend.entity.Workspace.RawPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RawPictureRepository extends JpaRepository<RawPicture, Long> {
    List<RawPicture> findByWorkspaceId(Long workspaceId);
    RawPicture findByIdAndWorkspaceId(Long id, Long workspaceId);
    void deleteByIdAndWorkspaceId(Long id, Long workspaceId);

    RawPicture findByWorkspaceIdAndId(Long workspaceId, Long id);
}