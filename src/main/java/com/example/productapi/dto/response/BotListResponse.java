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
public class BotListResponse {
    private Long id;
    private String externalKey;
    private String slug;
    private String name; // Based on language preference
    private String summary; // Based on language preference
    private String image;
    private String affiliateLink;
    private List<String> tags;
    private Instant createdAt;
    private Instant updatedAt;
}