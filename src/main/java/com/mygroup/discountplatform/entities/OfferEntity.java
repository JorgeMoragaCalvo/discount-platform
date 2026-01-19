package com.mygroup.discountplatform.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "offer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OfferEntity {

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
    @NotBlank(message = "discount percentage is required")
    private Float discountPercentage;

    private LocalDateTime validFrom;
    private LocalDateTime validTo;

    @Column(name = "quantity_available")
    private int quantityAvailable;
}
