package com.example.personal_blog.repository;

import com.example.personal_blog.model.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByArticleIdOrderByCreatedAtDesc(Long articleId, Pageable pageable);
    int countByArticleId(Long articleId);
}
