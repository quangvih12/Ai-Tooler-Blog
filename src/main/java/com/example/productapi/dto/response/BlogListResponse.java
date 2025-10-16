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
public class BlogListResponse {
    private Long id;
    private String slug;
    private String title; // Based on language preference
    private List<String> tags;
    private String image;
    private String excerpt; // Based on language preference
    private String status;
    private Instant publishedAt;
    private Instant createdAt;
    private String content_raw; // Based on language preference
    private String content_html; // Based on language preference
    private String content_text; // Based on language preference
}