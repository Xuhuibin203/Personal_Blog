package com.example.personal_blog.service;

import com.example.personal_blog.common.ApiResponse;
import com.example.personal_blog.model.entity.User;
import com.example.personal_blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse<?> updateAvatar(MultipartFile file, User user) throws IOException {
        if (file.isEmpty()) {
            return ApiResponse.error("文件不能为空");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ApiResponse.error("只支持图片文件");
        }

        // 使用绝对路径避免相对路径问题
        Path uploadDir = Paths.get(uploadPath).toAbsolutePath().resolve("avatars");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // 生成唯一文件名
        String originalName = file.getOriginalFilename();
        String extension = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : ".png";
        String fileName = "avatar_" + user.getId() + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;

        // 保存文件
        Path filePath = uploadDir.resolve(fileName);
        file.transferTo(filePath.toFile());

        // 删除旧头像
        if (user.getAvatarUrl() != null) {
            try {
                String oldFileName = user.getAvatarUrl().substring(user.getAvatarUrl().lastIndexOf("/") + 1);
                Path oldFilePath = uploadDir.resolve(oldFileName);
                Files.deleteIfExists(oldFilePath);
            } catch (Exception ignored) {
            }
        }

        // 更新用户头像
        String avatarUrl = "/uploads/avatars/" + fileName;
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);

        return ApiResponse.success(buildUserInfo(user));
    }

    public ApiResponse<?> updateProfile(String nickname, User user) {
        if (nickname != null && !nickname.isBlank()) {
            user.setNickname(nickname);
            userRepository.save(user);
        }
        return ApiResponse.success(buildUserInfo(user));
    }

    public ApiResponse<?> changePassword(String oldPassword, String newPassword, User user) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ApiResponse.error("原密码错误");
        }
        if (newPassword == null || newPassword.length() < 6) {
            return ApiResponse.error("新密码至少6位");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ApiResponse.success("密码修改成功", null);
    }

    private Map<String, Object> buildUserInfo(User user) {
        Map<String, Object> info = new HashMap<>();
        info.put("id", user.getId());
        info.put("username", user.getUsername());
        info.put("nickname", user.getNickname());
        info.put("avatarUrl", user.getAvatarUrl());
        info.put("role", user.getRole().name());
        info.put("createdAt", user.getCreatedAt());
        return info;
    }
}
