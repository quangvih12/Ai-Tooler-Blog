package com.example.productapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BotResponse {
    private Long id;
    private String externalId;
    private String externalKey;
    private String slug;
    private String image;
    private String affiliateLink;
    private MultiLangText name;
    private MultiLangText summary;
    private List<String> tags;
    private List<MultiLangItem> features;
    private List<MultiLangItem> strengths;
    private List<MultiLangItem> weaknesses;
    private List<MultiLangItem> targetUsers;
    private List<PricingPlan> pricingPlans;
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
    public static class MultiLangItem {
        private Long id;
        private String contentVi;
        private String contentEn;
        private Integer displayOrder;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PricingPlan {
        private Long id;
        private MultiLangText plan;
        private MultiLangText priceText;
        private BigDecimal amount;
        private String currency;
        private String interval;
        private Integer displayOrder;
    }
}