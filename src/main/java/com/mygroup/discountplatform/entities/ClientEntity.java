package com.mygroup.discountplatform.entities;

import com.mygroup.discountplatform.entities.enums.Genre;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
@Table(name = "clients", indexes = {
        @Index(name = "idx_client_mail", columnList = "email"),
        @Index(name = "idx_client_rut", columnList = "rut"),
        @Index(name = "idx_client_building", columnList = "id_building")
})
public class ClientEntity extends BaseEntity {

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

    @Column(name = "genre")
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_building", nullable = false)
    private BuildingEntity building;

    @ManyToMany
    @JoinTable(
            name = "client_hobby",
            joinColumns = @JoinColumn(name = "id_client"),
            inverseJoinColumns = @JoinColumn(name = "id_hobby")
    )
    private Set<HobbyEntity> hobbies = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "client_profession",
            joinColumns = @JoinColumn(name = "id_client"),
            inverseJoinColumns = @JoinColumn(name = "id_profession")
    )
    private Set<ProfessionEntity> professions = new HashSet<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RedemptionEntity> redemptions = new HashSet<>();
}
