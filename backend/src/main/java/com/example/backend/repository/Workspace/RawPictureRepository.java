package com.example.backend.repository.Workspace;

import com.example.backend.entity.Workspace.RawPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RawPictureRepository extends JpaRepository<RawPicture, Long> {
    List<RawPicture> findByWorkId(Long workId);
    RawPicture findByIdAndWorkId(Long id, Long workId);
    void deleteByIdAndWorkId(Long id, Long workId);

    RawPicture findByWorkIdAndId(Long worksId, Long id);
}