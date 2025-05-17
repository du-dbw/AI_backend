package com.example.backend.repository.Works;

import com.example.backend.entity.Works.Works;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface WorksRepository extends JpaRepository<Works, Long> {
    // 根据作品 ID 查找作品
    Works findByWorkId(Long workId);

    // 根据用户 ID 查找该用户的所有作品
    List<Works> findByUserId(Long userId);

    // 根据作品 ID 删除作品（修复点）
    @Modifying
    @Query("DELETE FROM Works w WHERE w.workId = ?1")
    @Transactional
    void deleteByWorkId(Long workId);

    // 检查作品是否存在
    boolean existsByWorkId(Long workId);

    // 根据用户 ID 统计作品数量
    Long countByUserId(Long userId);

    // 根据用户关注列表获取作品（假设关注列表是用户ID的集合）
    List<Works> findByUserIdIn(List<Long> followingUserIds, Pageable pageable);

    // 获取最新发布的作品（按createdTime降序排序）
    List<Works> findAllByOrderByCreatedTimeDesc(Pageable pageable);

    // 获取热度最高的作品（按likes和comments降序排序）
    List<Works> findAllByOrderByLikesDescCommentsDesc(Pageable pageable);

    // 综合排序（可以根据需要调整排序逻辑）
    List<Works> findAllByOrderByCreatedTimeDescLikesDescCommentsDesc(Pageable pageable);

    // 根据发布状态筛选作品
    List<Works> findByPublishedTrue(Pageable pageable); // 已发布的作品
    List<Works> findByPublishedFalse(Pageable pageable); // 未发布的作品（草稿）
    @Query("SELECT w FROM Works w")
    List<Works> MYfindAll(Pageable pageable); // 所有作品

    List<Works> findByUserIdAndPublishedTrue(Long id, Pageable pageable);


    List<Works> findAllByPublishedTrueOrderByCreatedTimeDesc(Pageable pageable);
    List<Works> findAllByPublishedTrueOrderByLikesDesc(Pageable pageable);
    List<Works> findAllByPublishedTrueOrderByCollectionCountDesc(Pageable pageable);
    List<Works> findAllByPublishedTrueOrderByCommentsDesc(Pageable pageable);


    List<Works> searchByTitleContainingOrderByLikesDesc(String keyword, Pageable pageable);

    List<Works> searchByTitleContainingOrderByCollectionCountDesc(String keyword, Pageable pageable);

    List<Works> searchByTitleContainingOrderByCommentsDesc(String keyword, Pageable pageable);

    List<Works> searchByTitleContainingOrderByCreatedTimeDesc(String keyword, Pageable pageable);


    default List<Works> findSimilarWorksByTitle(Long workId, double similarityThreshold) {
        Works targetWork = findByWorkId(workId);
        if (targetWork == null) {
            return Collections.emptyList();
        }
        String targetTitle = targetWork.getTitle();

        LevenshteinDistance levenshteinDistance = LevenshteinDistance.getDefaultInstance();

        // 创建一个Map来存储相似度
        Map<Works, Double> similarityMap = new HashMap<>();

        return findAll().stream()
                .filter(work -> !work.getWorkId().equals(workId)) // 排除当前作品本身
                .peek(work -> {
                    String currentTitle = work.getTitle();
                    int distance = levenshteinDistance.apply(targetTitle, currentTitle);
                    double similarity = 1.0 - (double) distance / Math.max(targetTitle.length(), currentTitle.length());
                    similarityMap.put(work, similarity);
                })
                .filter(work -> similarityMap.get(work) >= similarityThreshold)
                .sorted((work1, work2) -> Double.compare(similarityMap.get(work2), similarityMap.get(work1))) // 降序排序
                .limit(10) // 限制最多返回10个
                .collect(Collectors.toList());
    }

    List<Works> findByWorkIdIn(List<Long> workIds);

}