package com.mygroup.discountplatform.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "offer", indexes = {
        @Index(name = "idx_offer_supplier", columnList = "id_supplier"),
        @Index(name = "idx_offer_valid_from", columnList = "valid_from"),
        @Index(name = "idx_offer_valid_to", columnList = "valid_to")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OfferEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_offer")
    private Long id;

    @Column(name = "title", nullable = false)
    @NotBlank(message = "offer title is required")
    private String title;

    @Column(name = "description", nullable = false)
    @NotBlank(message = "offer description is required")
    private String description;

    @Column(name = "discount_percentage", nullable = false)
    @NotNull(message = "discount percentage is required")
    private Float discountPercentage;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @Column(name = "quantity_available")
    private int quantityAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_supplier", nullable = false)
    private SupplierEntity supplier;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RedemptionEntity> redemptions = new HashSet<>();
}
