package com.example.productapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bots")
@Data
@EqualsAndHashCode(exclude = {"features", "strengths", "weaknesses", "targetUsers", "pricingPlans"})
@ToString(exclude = {"features", "strengths", "weaknesses", "targetUsers", "pricingPlans"})
public class Bot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", unique = true)
    private String externalId;

    @Column(name = "external_key", nullable = false)
    private String externalKey;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "image", length = 500)
    private String image;

    @Column(name = "affiliate_link", length = 500)
    private String affiliateLink;

    @Column(name = "name_vi")
    private String nameVi;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "summary_vi", columnDefinition = "TEXT")
    private String summaryVi;

    @Column(name = "summary_en", columnDefinition = "TEXT")
    private String summaryEn;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "tags", columnDefinition = "text[]")
    private String[] tags;

    @OneToMany(mappedBy = "bot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("displayOrder ASC")
    private List<BotFeature> features = new ArrayList<>();

    @OneToMany(mappedBy = "bot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("displayOrder ASC")
    private List<BotStrength> strengths = new ArrayList<>();

    @OneToMany(mappedBy = "bot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("displayOrder ASC")
    private List<BotWeakness> weaknesses = new ArrayList<>();

    @OneToMany(mappedBy = "bot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("displayOrder ASC")
    private List<BotTargetUser> targetUsers = new ArrayList<>();

    @OneToMany(mappedBy = "bot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("displayOrder ASC")
    private List<BotPricing> pricingPlans = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Helper methods
    public void addFeature(BotFeature feature) {
        features.add(feature);
        feature.setBot(this);
    }

    public void removeFeature(BotFeature feature) {
        features.remove(feature);
        feature.setBot(null);
    }

    public void addStrength(BotStrength strength) {
        strengths.add(strength);
        strength.setBot(this);
    }

    public void removeStrength(BotStrength strength) {
        strengths.remove(strength);
        strength.setBot(null);
    }

    public void addWeakness(BotWeakness weakness) {
        weaknesses.add(weakness);
        weakness.setBot(this);
    }

    public void removeWeakness(BotWeakness weakness) {
        weaknesses.remove(weakness);
        weakness.setBot(null);
    }

    public void addTargetUser(BotTargetUser targetUser) {
        targetUsers.add(targetUser);
        targetUser.setBot(this);
    }

    public void removeTargetUser(BotTargetUser targetUser) {
        targetUsers.remove(targetUser);
        targetUser.setBot(null);
    }

    public void addPricing(BotPricing pricing) {
        pricingPlans.add(pricing);
        pricing.setBot(this);
    }

    public void removePricing(BotPricing pricing) {
        pricingPlans.remove(pricing);
        pricing.setBot(null);
    }

    // Constructors
    public Bot() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(String externalKey) {
        this.externalKey = externalKey;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAffiliateLink() {
        return affiliateLink;
    }

    public void setAffiliateLink(String affiliateLink) {
        this.affiliateLink = affiliateLink;
    }

    public String getNameVi() {
        return nameVi;
    }

    public void setNameVi(String nameVi) {
        this.nameVi = nameVi;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getSummaryVi() {
        return summaryVi;
    }

    public void setSummaryVi(String summaryVi) {
        this.summaryVi = summaryVi;
    }

    public String getSummaryEn() {
        return summaryEn;
    }

    public void setSummaryEn(String summaryEn) {
        this.summaryEn = summaryEn;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public List<BotFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<BotFeature> features) {
        this.features = features;
    }

    public List<BotStrength> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<BotStrength> strengths) {
        this.strengths = strengths;
    }

    public List<BotWeakness> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<BotWeakness> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public List<BotTargetUser> getTargetUsers() {
        return targetUsers;
    }

    public void setTargetUsers(List<BotTargetUser> targetUsers) {
        this.targetUsers = targetUsers;
    }

    public List<BotPricing> getPricingPlans() {
        return pricingPlans;
    }

    public void setPricingPlans(List<BotPricing> pricingPlans) {
        this.pricingPlans = pricingPlans;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}