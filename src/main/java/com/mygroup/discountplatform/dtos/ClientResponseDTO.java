package com.mygroup.discountplatform.dtos;

import com.mygroup.discountplatform.entities.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Response DTO for client details")
public class ClientResponseDTO {

    @Schema(description = "Unique identifier of the client", example = "1")
    private Long id;

    @Schema(description = "Timestamp when the client was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the client was last updated", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Client's RUT identifier", example = "12345678-9")
    private String rut;

    @Schema(description = "Client's first name", example = "Juan")
    private String firstName;

    @Schema(description = "Client's paternal last name", example = "Pérez")
    private String lastNamePaternal;

    @Schema(description = "Client's maternal last name", example = "González")
    private String lastNameMaternal;

    @Schema(description = "Client's email address", example = "juan.perez@email.com")
    private String email;

    @Schema(description = "Client's phone number", example = "+56912345678")
    private String phone;

    @Schema(description = "Department number in the building", example = "1201")
    private String departmentNumber;

    @Schema(description = "Client's genre", example = "MALE")
    private Genre genre;

    @Schema(description = "ID of the building where the client resides", example = "1")
    private Long buildingId;

    @Schema(description = "Name of the building where the client resides", example = "Central Tower")
    private String buildingName;

    @Schema(description = "List of hobbies associated with the client")
    private List<HobbyDTO> hobbies;

    @Schema(description = "List of professions associated with the client")
    private List<ProfessionDTO> professions;
}
