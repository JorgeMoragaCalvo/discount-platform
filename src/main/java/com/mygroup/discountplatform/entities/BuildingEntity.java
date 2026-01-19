package com.mygroup.discountplatform.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "building", indexes = {
        @Index(name = "idx_building_city", columnList = "city")
})
@Schema(description = "Building entity representing a physical building location")
public class BuildingEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_building")
    @Schema(description = "Unique identifier of the building", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "name is required")
    @Schema(description = "Name of the building", example = "Central Tower", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Column(name = "address", nullable = false)
    @NotBlank(message = "address is required")
    @Schema(description = "Street address of the building", example = "123 Main Street", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @Column(name = "city", nullable = false)
    @NotBlank(message = "city is required")
    @Schema(description = "City where the building is located", example = "Madrid", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Clients associated with this building", accessMode = Schema.AccessMode.READ_ONLY)
    private Set<ClientEntity> clients = new HashSet<>();

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Suppliers associated with this building", accessMode = Schema.AccessMode.READ_ONLY)
    private Set<BuildingSupplierEntity> buildingSuppliers = new HashSet<>();
}
