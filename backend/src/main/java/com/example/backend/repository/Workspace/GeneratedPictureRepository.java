package com.example.backend.repository.Workspace;

import com.example.backend.entity.Workspace.GeneratedPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneratedPictureRepository extends JpaRepository<GeneratedPicture, Long> {
    List<GeneratedPicture> findByWorkId(Long workId);
    GeneratedPicture findByIdAndWorkId(Long id, Long workId);
    void deleteByIdAndWorkId(Long id, Long workId);

    GeneratedPicture findByWorkIdAndId(Long workId, Long pictureId);
}
