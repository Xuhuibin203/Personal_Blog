package com.example.personal_blog.service;

import com.example.personal_blog.common.ApiResponse;
import com.example.personal_blog.model.dto.ArticleRequest;
import com.example.personal_blog.model.entity.Article;
import com.example.personal_blog.model.entity.User;
import com.example.personal_blog.model.enums.ArticleType;
import com.example.personal_blog.repository.ArticleRepository;
import com.example.personal_blog.repository.CommentRepository;
import com.example.personal_blog.repository.LikeRecordRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final LikeRecordRepository likeRecordRepository;
    private final CommentRepository commentRepository;

    public ArticleService(ArticleRepository articleRepository,
                          LikeRecordRepository likeRecordRepository,
                          CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.likeRecordRepository = likeRecordRepository;
        this.commentRepository = commentRepository;
    }

    public ApiResponse<?> getArticles(int page, int size, String type, User currentUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        var articlesPage = (type != null && !type.isEmpty())
                ? articleRepository.findByTypeAndIsPublishedTrueOrderByCreatedAtDesc(ArticleType.valueOf(type), pageable)
                : articleRepository.findByIsPublishedTrueOrderByCreatedAtDesc(pageable);

        List<Map<String, Object>> content = articlesPage.getContent().stream()
                .map(article -> buildArticleInfo(article, currentUser))
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalElements", articlesPage.getTotalElements());
        result.put("totalPages", articlesPage.getTotalPages());
        result.put("size", articlesPage.getSize());
        result.put("number", articlesPage.getNumber());

        return ApiResponse.success(result);
    }

    public ApiResponse<?> getArticle(Long id, User currentUser) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null || !article.isPublished()) {
            return ApiResponse.error(404, "文章不存在");
        }
        return ApiResponse.success(buildArticleInfo(article, currentUser));
    }

    public ApiResponse<?> createArticle(ArticleRequest request, User author) {
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setSummary(request.getSummary());
        article.setCoverImage(request.getCoverImage());
        article.setType(request.getType());
        article.setExternalLink(request.getExternalLink());
        article.setAuthor(author);

        article = articleRepository.save(article);
        return ApiResponse.success(buildArticleInfo(article, author));
    }

    public ApiResponse<?> updateArticle(Long id, ArticleRequest request) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return ApiResponse.error(404, "文章不存在");
        }

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setSummary(request.getSummary());
        article.setCoverImage(request.getCoverImage());
        article.setType(request.getType());
        article.setExternalLink(request.getExternalLink());

        article = articleRepository.save(article);
        return ApiResponse.success(buildArticleInfo(article, article.getAuthor()));
    }

    @Transactional
    public ApiResponse<?> deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            return ApiResponse.error(404, "文章不存在");
        }
        // 先清理关联数据，否则外键约束会阻止删除文章
        commentRepository.deleteByArticleId(id);
        likeRecordRepository.deleteByArticleId(id);
        articleRepository.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }

    @Transactional
    public ApiResponse<?> toggleLike(Long articleId, User user) {
        Article article = articleRepository.findById(articleId).orElse(null);
        if (article == null) {
            return ApiResponse.error(404, "文章不存在");
        }

        boolean alreadyLiked = likeRecordRepository.existsByArticleIdAndUserId(articleId, user.getId());
        if (alreadyLiked) {
            // 取消点赞
            likeRecordRepository.deleteByArticleIdAndUserId(articleId, user.getId());
            article.setLikeCount(Math.max(0, article.getLikeCount() - 1));
        } else {
            // 点赞（捕获重复点赞的并发冲突）
            try {
                com.example.personal_blog.model.entity.LikeRecord record =
                        new com.example.personal_blog.model.entity.LikeRecord();
                record.setArticle(article);
                record.setUser(user);
                likeRecordRepository.save(record);
                article.setLikeCount(article.getLikeCount() + 1);
            } catch (Exception e) {
                // 唯一约束冲突：已经点过赞了，忽略
            }
        }

        articleRepository.save(article);

        // 刷新后的实际点赞状态
        boolean liked = likeRecordRepository.existsByArticleIdAndUserId(articleId, user.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("liked", liked);
        result.put("count", article.getLikeCount());
        return ApiResponse.success(result);
    }

    @Transactional
    public ApiResponse<?> removeLike(Long articleId, User user) {
        Article article = articleRepository.findById(articleId).orElse(null);
        if (article == null) {
            return ApiResponse.error(404, "文章不存在");
        }

        if (likeRecordRepository.existsByArticleIdAndUserId(articleId, user.getId())) {
            likeRecordRepository.deleteByArticleIdAndUserId(articleId, user.getId());
            article.setLikeCount(Math.max(0, article.getLikeCount() - 1));
            articleRepository.save(article);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("liked", false);
        result.put("count", article.getLikeCount());
        return ApiResponse.success(result);
    }

    private Map<String, Object> buildArticleInfo(Article article, User currentUser) {
        Map<String, Object> info = new HashMap<>();
        info.put("id", article.getId());
        info.put("title", article.getTitle());
        info.put("content", article.getContent());
        info.put("summary", article.getSummary());
        info.put("coverImage", article.getCoverImage());
        info.put("type", article.getType().name());
        info.put("externalLink", article.getExternalLink());
        info.put("authorId", article.getAuthor().getId());
        info.put("authorName", article.getAuthor().getNickname());
        info.put("authorAvatar", article.getAuthor().getAvatarUrl());
        info.put("isPublished", article.isPublished());
        info.put("likeCount", article.getLikeCount());
        info.put("commentCount", article.getCommentCount());
        info.put("createdAt", article.getCreatedAt());
        info.put("updatedAt", article.getUpdatedAt());

        // 当前用户是否已点赞
        boolean isLiked = currentUser != null &&
                likeRecordRepository.existsByArticleIdAndUserId(article.getId(), currentUser.getId());
        info.put("isLiked", isLiked);

        return info;
    }
}
