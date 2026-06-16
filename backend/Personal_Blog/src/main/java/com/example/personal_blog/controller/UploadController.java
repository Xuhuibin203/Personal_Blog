package com.example.personal_blog.controller;

import com.example.personal_blog.common.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    @Value("${upload.path:./uploads}")
    private String uploadPath;

    @PostMapping
    public ApiResponse<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String url = saveFile(file, "images");
            Map<String, String> result = new HashMap<>();
            result.put("url", url);
            return ApiResponse.success(result);
        } catch (IOException e) {
            log.error("上传图片失败: {}", e.getMessage(), e);
            return ApiResponse.error(500, "上传失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch")
    public ApiResponse<?> uploadImages(@RequestParam("files") MultipartFile[] files) {
        try {
            List<String> urls = new ArrayList<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    urls.add(saveFile(file, "images"));
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("urls", urls);
            return ApiResponse.success(result);
        } catch (IOException e) {
            log.error("批量上传图片失败: {}", e.getMessage(), e);
            return ApiResponse.error(500, "批量上传失败: " + e.getMessage());
        }
    }

    private String saveFile(MultipartFile file, String subDir) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("只支持图片文件");
        }

        // 使用绝对路径避免相对路径解析到 Tomcat 临时目录
        Path uploadDir = Paths.get(uploadPath).toAbsolutePath().resolve(subDir);
        log.info("上传目录: {}", uploadDir);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
            log.info("创建上传目录: {}", uploadDir);
        }

        String originalName = file.getOriginalFilename();
        String extension = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf("."))
                : ".png";
        String fileName = UUID.randomUUID().toString().substring(0, 8) + "_" + System.currentTimeMillis() + extension;

        Path filePath = uploadDir.resolve(fileName);
        file.transferTo(filePath.toFile());

        return "/uploads/" + subDir + "/" + fileName;
    }
}
