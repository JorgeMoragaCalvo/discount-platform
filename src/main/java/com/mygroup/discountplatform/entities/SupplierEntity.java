package com.mygroup.discountplatform.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "supplier")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SupplierEntity {

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
}
