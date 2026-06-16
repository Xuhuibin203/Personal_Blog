package com.example.personal_blog.controller;

import com.example.personal_blog.common.ApiResponse;
import com.example.personal_blog.model.entity.User;
import com.example.personal_blog.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/avatar")
    public ApiResponse<?> updateAvatar(@RequestParam("file") MultipartFile file,
                                       @AuthenticationPrincipal User user) throws IOException {
        return userService.updateAvatar(file, user);
    }

    @PutMapping("/profile")
    public ApiResponse<?> updateProfile(@RequestBody Map<String, String> body,
                                        @AuthenticationPrincipal User user) {
        return userService.updateProfile(body.get("nickname"), user);
    }

    @PutMapping("/password")
    public ApiResponse<?> changePassword(@RequestBody Map<String, String> body,
                                         @AuthenticationPrincipal User user) {
        return userService.changePassword(body.get("oldPassword"), body.get("newPassword"), user);
    }
}
