package com.example.personal_blog.service;

import com.example.personal_blog.common.ApiResponse;
import com.example.personal_blog.model.dto.LoginRequest;
import com.example.personal_blog.model.dto.RegisterRequest;
import com.example.personal_blog.model.entity.User;
import com.example.personal_blog.model.enums.UserRole;
import com.example.personal_blog.repository.UserRepository;
import com.example.personal_blog.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public ApiResponse<?> login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ApiResponse.error(401, "用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", buildUserInfo(user));
        return ApiResponse.success(data);
    }

    public ApiResponse<?> register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ApiResponse.error("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setRole(UserRole.USER);

        user = userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", buildUserInfo(user));
        return ApiResponse.success(data);
    }

    public ApiResponse<?> getMe(User user) {
        return ApiResponse.success(buildUserInfo(user));
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
