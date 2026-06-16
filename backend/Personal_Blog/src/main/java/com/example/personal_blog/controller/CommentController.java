package com.example.personal_blog.controller;

import com.example.personal_blog.common.ApiResponse;
import com.example.personal_blog.model.dto.CommentRequest;
import com.example.personal_blog.model.entity.User;
import com.example.personal_blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/articles/{articleId}/comments")
    public ApiResponse<?> getComments(@PathVariable Long articleId,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        return commentService.getComments(articleId, page, size);
    }

    @PostMapping("/articles/{articleId}/comments")
    public ApiResponse<?> createComment(@PathVariable Long articleId,
                                        @Valid @RequestBody CommentRequest request,
                                        @AuthenticationPrincipal User user) {
        return commentService.createComment(articleId, request, user);
    }

    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<?> deleteComment(@PathVariable Long commentId,
                                        @AuthenticationPrincipal User user) {
        return commentService.deleteComment(commentId, user);
    }
}
