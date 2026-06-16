package com.example.personal_blog.controller;

import com.example.personal_blog.common.ApiResponse;
import com.example.personal_blog.model.dto.ArticleRequest;
import com.example.personal_blog.model.entity.User;
import com.example.personal_blog.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ApiResponse<?> getArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @AuthenticationPrincipal User currentUser) {
        return articleService.getArticles(page, size, type, currentUser);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getArticle(@PathVariable Long id,
                                     @AuthenticationPrincipal User currentUser) {
        return articleService.getArticle(id, currentUser);
    }

    @PostMapping
    public ApiResponse<?> createArticle(@Valid @RequestBody ArticleRequest request,
                                        @AuthenticationPrincipal User author) {
        return articleService.createArticle(request, author);
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateArticle(@PathVariable Long id,
                                        @Valid @RequestBody ArticleRequest request) {
        return articleService.updateArticle(id, request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteArticle(@PathVariable Long id) {
        return articleService.deleteArticle(id);
    }

    @PostMapping("/{id}/like")
    public ApiResponse<?> toggleLike(@PathVariable Long id,
                                     @AuthenticationPrincipal User user) {
        return articleService.toggleLike(id, user);
    }

    @DeleteMapping("/{id}/like")
    public ApiResponse<?> removeLike(@PathVariable Long id,
                                     @AuthenticationPrincipal User user) {
        return articleService.removeLike(id, user);
    }
}
