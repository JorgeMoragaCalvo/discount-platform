package com.mygroup.discountplatform.dtos;

import com.mygroup.discountplatform.entities.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO for client list items")
public class ClientListDTO {

    @Schema(description = "Unique identifier of the client", example = "1")
    private Long id;

    @Schema(description = "Client's RUT identifier", example = "12345678-9")
    private String rut;

    @Schema(description = "Client's first name", example = "Juan")
    private String firstName;

    @Schema(description = "Client's paternal last name", example = "Pérez")
    private String lastNamePaternal;

    @Schema(description = "Client's email address", example = "juan.perez@email.com")
    private String email;

    @Schema(description = "Client's genre", example = "MALE")
    private Genre genre;

    @Schema(description = "Name of the building where the client resides", example = "Central Tower")
    private String buildingName;
}
