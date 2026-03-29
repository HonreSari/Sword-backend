package org.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;                    // hashed password

    @Column(name = "credit_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal creditBalance = BigDecimal.ZERO;

    // Current active VIP Tier (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "active_vip_tier_id")
    private VipTier activeVipTier;

    @Column(name = "vip_expiry_date")
    private LocalDateTime vipExpiryDate;

    private String role = "USER";               // USER or ADMIN

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Helper method
    public boolean hasActiveVip() {
        return activeVipTier != null &&
                (vipExpiryDate == null || vipExpiryDate.isAfter(LocalDateTime.now()));
    }
}
