package com.mygroup.discountplatform.entities;

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
public class BuildingEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_building")
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "name is required")
    private String name;

    @Column(name = "address", nullable = false)
    @NotBlank(message = "address is required")
    private String address;

    @Column(name = "city", nullable = false)
    @NotBlank(message = "city is required")
    private String city;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClientEntity> clients = new HashSet<>();

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BuildingSupplierEntity> buildingSuppliers = new HashSet<>();
}
