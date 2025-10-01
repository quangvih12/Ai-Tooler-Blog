package com.example.productapi.dto.response;

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
public class BlogResponse {
    private Long id;
    private String externalId;
    private String slug;
    private MultiLangText title;
    private List<String> tags;
    private String image;
    private MultiLangText excerpt;
    private MultiLangContent content;
    private String status;
    private Instant publishedAt;
    private String source;
    private String sourceUrl;
    private Instant createdAt;
    private Instant updatedAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MultiLangText {
        private String vi;
        private String en;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MultiLangContent {
        private ContentDetail vi;
        private ContentDetail en;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContentDetail {
        private String raw;
        private String html;
        private String text;
    }
}