package com.mygroup.discountplatform.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "building_supplier")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BuildingSupplierEntity {

    @EmbeddedId
    private BuildingSupplierId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("buildingId")
    @JoinColumn(name = "id_building")
    private BuildingEntity building;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("supplierId")
    @JoinColumn(name = "id_supplier")
    private SupplierEntity supplier;

    @Column(name = "joined_at", nullable = false)
    @NotNull(message = "joined_at is required")
    private LocalDate joinedAt;
}
