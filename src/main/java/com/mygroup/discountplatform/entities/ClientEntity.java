package com.mygroup.discountplatform.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "client_")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Long id;

    @Column(name = "rut", nullable = false, unique = true)
    @NotBlank
    private String rut;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String firstName;

    @Column(name = "last_name_paternal", nullable = false)
    @NotBlank(message = "last name paternal is required")
    private String lastNamePaternal;

    @Column(name = "last_name_maternal")
    private String lastNameMaternal;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Invalid email format")
    @NotBlank
    private String email;

    @Column(name = "phone")
    @Pattern(regexp = "^\\+?[0-9]{9,12}$", message = "Invalid phone format")
    private String phone;

    @Column(name = "department_number")
    private String departmentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_building", nullable = false)
    private BuildingEntity building;
}
