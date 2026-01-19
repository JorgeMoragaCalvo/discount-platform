package com.mygroup.discountplatform.entities;

import com.mygroup.discountplatform.entities.enums.RedemptionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "redemption", indexes = {
        @Index(name = "idx_redemption_offer", columnList = "id_offer"),
        @Index(name = "idx_redemption_client", columnList = "id_client"),
        @Index(name = "idx_redemption_status", columnList = "status"),
        @Index(name = "idx_redemption_redeemed_at", columnList = "redeemed_at")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RedemptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_redemption")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_offer", nullable = false)
    @NotNull(message = "offer is required")
    private OfferEntity offer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false)
    @NotNull(message = "client is required")
    private ClientEntity client;

    @Column(name = "redeemed_at", nullable = false)
    @NotNull(message = "redeemed_at is required")
    private LocalDateTime redeemedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull(message = "status is required")
    private RedemptionStatus status;
}
