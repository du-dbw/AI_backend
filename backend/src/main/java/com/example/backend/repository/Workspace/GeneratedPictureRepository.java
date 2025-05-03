package com.example.backend.repository.Workspace;

import com.example.backend.entity.Workspace.GeneratedPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneratedPictureRepository extends JpaRepository<GeneratedPicture, Long> {
    List<GeneratedPicture> findByWorkspaceId(Long workspaceId);
    GeneratedPicture findByIdAndWorkspaceId(Long id, Long workspaceId);
    void deleteByIdAndWorkspaceId(Long id, Long workspaceId);

    GeneratedPicture findByWorkspaceIdAndId(Long workspaceId, Long pictureId);
}
