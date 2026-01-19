package com.mygroup.discountplatform.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "supplier", indexes = {
        @Index(name = "idx_supplier_category", columnList = "id_category")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SupplierEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_supplier")
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "supplier name is required")
    private String name;

    @Column(name = "address", nullable = false)
    @NotBlank(message = "supplier address is required")
    private String address;

    @Column(name = "email", nullable = false,  unique = true)
    @Email(message = "Invalid email format")
    @NotBlank(message = "contact email is required")
    private String contactEmail;

    @Column(name = "phone", nullable = false)
    @Pattern(regexp = "^\\+?[0-9]{9,12}$", message = "Invalid phone format")
    @NotBlank(message = "supplier phone is required")
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OfferEntity> offers = new HashSet<>();

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BuildingSupplierEntity> buildingSuppliers = new HashSet<>();
}
