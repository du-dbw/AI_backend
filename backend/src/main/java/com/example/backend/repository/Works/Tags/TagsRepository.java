package com.example.backend.repository.Works.Tags;

import com.example.backend.entity.Works.Tags.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

public interface TagsRepository extends JpaRepository<Tags, Long> {

    // 根据作品 ID 和标签名查找标签
    @Query("SELECT t FROM Tags t WHERE t.workId = ?1 AND t.name = ?2")
    Tags findByWorkIdAndName(Long workId, String name);

    // 根据作品 ID 删除所有标签
    @Modifying
    @Query("DELETE FROM Tags t WHERE t.workId = ?1")
    @Transactional
    void deleteByWorkId(Long workId);

    // 根据标签名查找所有标签
    List<Tags> findByName(String name);

    // 根据作品 ID 查找所有标签
    List<Tags> findByWorkId(Long workId);

    // 检查某个作品是否已有特定标签
    boolean existsByWorkIdAndName(Long workId, String name);

    // 删除特定作品的特定标签
    @Modifying
    @Query("DELETE FROM Tags t WHERE t.workId = ?1 AND t.name = ?2")
    @Transactional
    void deleteByWorkIdAndName(Long workId, String name);

}