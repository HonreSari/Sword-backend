package org.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vip_tiers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VipTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;                    // e.g. "Basic", "Premium", "Ultimate Sword Immortal"

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;               // Monthly price in USD or CNY

    @Column(name = "monthly_credits", nullable = false)
    private int monthlyCredits;

    @Column(length = 500)
    private String description;

    @ElementCollection
    @CollectionTable(
            name = "vip_tier_benefits",
            joinColumns = @JoinColumn(name = "vip_tier_id")
    )
    @Column(name = "benefit")
    private List<String> benefits = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
