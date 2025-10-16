package com.example.productapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogRequest {
    
    @NotBlank(message = "Slug is required")
    @Size(max = 255, message = "Slug must not exceed 255 characters")
    private String slug;
    
    private String titleVi;
    private String titleEn;
    
    private List<String> tags;
    
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String image;
    
    private String excerptVi;
    private String excerptEn;
    
    private BlogContent contentVi;
    private BlogContent contentEn;
    
    private String status = "active";
    
    @Size(max = 100, message = "Source must not exceed 100 characters")
    private String source;
    
    @Size(max = 500, message = "Source URL must not exceed 500 characters")
    private String sourceUrl;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BlogContent {
        private String raw;
        private String html;
        private String text;
    }
}