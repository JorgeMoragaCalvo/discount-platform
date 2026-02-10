package com.mygroup.discountplatform.dtos;

import com.mygroup.discountplatform.entities.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Request DTO for creating a new client")
public class ClientCreateRequestDTO {

    @NotBlank(message = "rut is required")
    @Schema(description = "Client's RUT identifier", example = "12345678-9", requiredMode = Schema.RequiredMode.REQUIRED)
    private String rut;

    @NotBlank(message = "first name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Schema(description = "Client's first name", example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotBlank(message = "last name paternal is required")
    @Schema(description = "Client's paternal last name", example = "Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastNamePaternal;

    @Schema(description = "Client's maternal last name", example = "González")
    private String lastNameMaternal;

    @NotBlank(message = "email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Client's email address", example = "juan.perez@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{9,12}$", message = "Invalid phone format")
    @Schema(description = "Client's phone number", example = "+56912345678")
    private String phone;

    @Schema(description = "Department number in the building", example = "1201")
    private String departmentNumber;

    @Schema(description = "Client's genre", example = "MALE")
    private Genre genre;

    @NotNull(message = "building id is required")
    @Schema(description = "ID of the building where the client resides", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long buildingId;

    @Schema(description = "Set of hobby IDs associated with the client", example = "[1, 2, 3]")
    private Set<Long> hobbyIds;

    @Size(max = 2, message = "A client can have at most 2 professions")
    @Schema(description = "Set of profession IDs associated with the client (max 2)", example = "[1, 2]")
    private Set<Long> professionIds;
}
