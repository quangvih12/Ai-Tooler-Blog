package com.example.productapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "bot_features")
@Data
@EqualsAndHashCode(exclude = "bot")
@ToString(exclude = "bot")
public class BotFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bot_id", nullable = false)
    private Bot bot;

    @Column(name = "content_vi", columnDefinition = "TEXT")
    private String contentVi;

    @Column(name = "content_en", columnDefinition = "TEXT")
    private String contentEn;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    // Constructors
    public BotFeature() {
    }

    public BotFeature(String contentVi, String contentEn, Integer displayOrder) {
        this.contentVi = contentVi;
        this.contentEn = contentEn;
        this.displayOrder = displayOrder;
    }

}