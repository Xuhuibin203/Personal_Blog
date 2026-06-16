package com.example.personal_blog.repository;

import com.example.personal_blog.model.entity.LikeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRecordRepository extends JpaRepository<LikeRecord, Long> {
    Optional<LikeRecord> findByArticleIdAndUserId(Long articleId, Long userId);
    boolean existsByArticleIdAndUserId(Long articleId, Long userId);
    int countByArticleId(Long articleId);
    void deleteByArticleIdAndUserId(Long articleId, Long userId);
}
