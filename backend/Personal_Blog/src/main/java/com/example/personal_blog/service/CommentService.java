package com.example.personal_blog.service;

import com.example.personal_blog.common.ApiResponse;
import com.example.personal_blog.model.dto.CommentRequest;
import com.example.personal_blog.model.entity.Article;
import com.example.personal_blog.model.entity.Comment;
import com.example.personal_blog.model.entity.User;
import com.example.personal_blog.repository.ArticleRepository;
import com.example.personal_blog.repository.CommentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    public ApiResponse<?> getComments(Long articleId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        var commentsPage = commentRepository.findByArticleIdOrderByCreatedAtDesc(articleId, pageable);

        List<Map<String, Object>> content = commentsPage.getContent().stream()
                .map(this::buildCommentInfo)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", commentsPage.getTotalElements());
        result.put("totalPages", commentsPage.getTotalPages());
        result.put("size", commentsPage.getSize());
        result.put("number", commentsPage.getNumber());

        return ApiResponse.success(result);
    }

    @Transactional
    public ApiResponse<?> createComment(Long articleId, CommentRequest request, User user) {
        Article article = articleRepository.findById(articleId).orElse(null);
        if (article == null) {
            return ApiResponse.error(404, "文章不存在");
        }

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setArticle(article);
        comment.setUser(user);
        comment.setParentId(request.getParentId());

        comment = commentRepository.save(comment);

        // 更新文章评论数
        article.setCommentCount(commentRepository.countByArticleId(articleId));
        articleRepository.save(article);

        return ApiResponse.success(buildCommentInfo(comment));
    }

    @Transactional
    public ApiResponse<?> deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            return ApiResponse.error(404, "评论不存在");
        }

        // 管理员或评论作者可以删除
        if (!user.getRole().name().equals("ADMIN") && !comment.getUser().getId().equals(user.getId())) {
            return ApiResponse.error(403, "无权删除此评论");
        }

        Article article = comment.getArticle();
        commentRepository.delete(comment);

        // 更新文章评论数
        article.setCommentCount(commentRepository.countByArticleId(article.getId()));
        articleRepository.save(article);

        return ApiResponse.success("删除成功", null);
    }

    private Map<String, Object> buildCommentInfo(Comment comment) {
        Map<String, Object> info = new HashMap<>();
        info.put("id", comment.getId());
        info.put("content", comment.getContent());
        info.put("articleId", comment.getArticle().getId());
        info.put("userId", comment.getUser().getId());
        info.put("username", comment.getUser().getNickname());
        info.put("userAvatar", comment.getUser().getAvatarUrl());
        info.put("parentId", comment.getParentId());
        info.put("createdAt", comment.getCreatedAt());
        return info;
    }
}
