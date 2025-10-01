package com.example.productapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BotRequest {
    
    @Size(max = 255, message = "External ID must not exceed 255 characters")
    private String externalId;
    
    @NotBlank(message = "External key is required")
    @Size(max = 255, message = "External key must not exceed 255 characters")
    private String externalKey;
    
    @NotBlank(message = "Slug is required")
    @Size(max = 255, message = "Slug must not exceed 255 characters")
    private String slug;
    
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String image;
    
    @Size(max = 500, message = "Affiliate link must not exceed 500 characters")
    private String affiliateLink;
    
    private String nameVi;
    private String nameEn;
    
    private String summaryVi;
    private String summaryEn;
    
    private List<String> tags;
    
    private List<FeatureRequest> features;
    private List<StrengthRequest> strengths;
    private List<WeaknessRequest> weaknesses;
    private List<TargetUserRequest> targetUsers;
    private List<PricingRequest> pricingPlans;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeatureRequest {
        private String contentVi;
        private String contentEn;
        private Integer displayOrder;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StrengthRequest {
        private String contentVi;
        private String contentEn;
        private Integer displayOrder;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeaknessRequest {
        private String contentVi;
        private String contentEn;
        private Integer displayOrder;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TargetUserRequest {
        private String contentVi;
        private String contentEn;
        private Integer displayOrder;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PricingRequest {
        private String planVi;
        private String planEn;
        private String priceTextVi;
        private String priceTextEn;
        private BigDecimal amount;
        private String currency = "USD";
        private String interval;
        private Integer displayOrder;
    }
}