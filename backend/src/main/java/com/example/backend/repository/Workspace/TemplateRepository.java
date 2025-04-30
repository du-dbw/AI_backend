package com.example.backend.repository.Workspace;

import com.example.backend.entity.Workspace.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    Template findByIdAndUserId(Long templateId, String userId);
    List<Template> findAllByUserIdAndNameLike(String userId, String keyword);
}