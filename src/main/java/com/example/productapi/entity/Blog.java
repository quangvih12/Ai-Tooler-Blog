package com.example.productapi.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", unique = true)
    private String externalId;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "title_vi", columnDefinition = "TEXT")
    private String titleVi;

    @Column(name = "title_en", columnDefinition = "TEXT")
    private String titleEn;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "tags", columnDefinition = "text[]")
    private String[] tags;

    @Column(name = "image", length = 500)
    private String image;

    @Column(name = "excerpt_vi", columnDefinition = "TEXT")
    private String excerptVi;

    @Column(name = "excerpt_en", columnDefinition = "TEXT")
    private String excerptEn;

    @Column(name = "content_vi_raw", columnDefinition = "TEXT")
    private String contentViRaw;

    @Column(name = "content_vi_html", columnDefinition = "TEXT")
    private String contentViHtml;

    @Column(name = "content_vi_text", columnDefinition = "TEXT")
    private String contentViText;

    @Column(name = "content_en_raw", columnDefinition = "TEXT")
    private String contentEnRaw;

    @Column(name = "content_en_html", columnDefinition = "TEXT")
    private String contentEnHtml;

    @Column(name = "content_en_text", columnDefinition = "TEXT")
    private String contentEnText;

    @Column(name = "status", length = 50)
    private String status = "active";

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "source", length = 100)
    private String source;

    @Column(name = "source_url", length = 500)
    private String sourceUrl;

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

    // Constructors
    public Blog() {
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitleVi() {
        return titleVi;
    }

    public void setTitleVi(String titleVi) {
        this.titleVi = titleVi;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExcerptVi() {
        return excerptVi;
    }

    public void setExcerptVi(String excerptVi) {
        this.excerptVi = excerptVi;
    }

    public String getExcerptEn() {
        return excerptEn;
    }

    public void setExcerptEn(String excerptEn) {
        this.excerptEn = excerptEn;
    }

    public String getContentViRaw() {
        return contentViRaw;
    }

    public void setContentViRaw(String contentViRaw) {
        this.contentViRaw = contentViRaw;
    }

    public String getContentViHtml() {
        return contentViHtml;
    }

    public void setContentViHtml(String contentViHtml) {
        this.contentViHtml = contentViHtml;
    }

    public String getContentViText() {
        return contentViText;
    }

    public void setContentViText(String contentViText) {
        this.contentViText = contentViText;
    }

    public String getContentEnRaw() {
        return contentEnRaw;
    }

    public void setContentEnRaw(String contentEnRaw) {
        this.contentEnRaw = contentEnRaw;
    }

    public String getContentEnHtml() {
        return contentEnHtml;
    }

    public void setContentEnHtml(String contentEnHtml) {
        this.contentEnHtml = contentEnHtml;
    }

    public String getContentEnText() {
        return contentEnText;
    }

    public void setContentEnText(String contentEnText) {
        this.contentEnText = contentEnText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
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