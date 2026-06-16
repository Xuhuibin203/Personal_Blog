package com.example.personal_blog.repository;

import com.example.personal_blog.model.entity.Article;
import com.example.personal_blog.model.enums.ArticleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByIsPublishedTrueOrderByCreatedAtDesc(Pageable pageable);
    Page<Article> findByTypeAndIsPublishedTrueOrderByCreatedAtDesc(ArticleType type, Pageable pageable);
}
