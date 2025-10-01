package com.example.productapi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "bot_pricing")
@Data
@EqualsAndHashCode(exclude = "bot")
@ToString(exclude = "bot")
public class BotPricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bot_id", nullable = false)
    private Bot bot;

    @Column(name = "plan_vi")
    private String planVi;

    @Column(name = "plan_en")
    private String planEn;

    @Column(name = "price_text_vi")
    private String priceTextVi;

    @Column(name = "price_text_en")
    private String priceTextEn;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", length = 10)
    private String currency = "USD";

    @Column(name = "interval", length = 50)
    private String interval;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    // Constructors
    public BotPricing() {
    }

}