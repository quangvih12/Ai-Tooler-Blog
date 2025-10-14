package com.example.productapi.dto.response;

import com.example.productapi.entity.Category;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryResponse {
    private Long id;
    private String title;
    private String desc;
    private List<Tags> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Tags {
        private Long id;
        private String name;
        private Long categoryId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;


    }
}
