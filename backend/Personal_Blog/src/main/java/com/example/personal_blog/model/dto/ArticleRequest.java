package com.example.personal_blog.model.dto;

import com.example.personal_blog.model.enums.ArticleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ArticleRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题最长100个字符")
    private String title;

    private String content;

    @NotBlank(message = "摘要不能为空")
    @Size(max = 200, message = "摘要最长200个字符")
    private String summary;

    private String coverImage;

    private ArticleType type = ArticleType.ARTICLE;

    private String externalLink;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public ArticleType getType() { return type; }
    public void setType(ArticleType type) { this.type = type; }

    public String getExternalLink() { return externalLink; }
    public void setExternalLink(String externalLink) { this.externalLink = externalLink; }
}
