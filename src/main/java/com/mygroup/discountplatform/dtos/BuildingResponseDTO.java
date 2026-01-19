package com.mygroup.discountplatform.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Response DTO for building creation")
public class BuildingResponseDTO {

    @Schema(description = "Timestamp when the building was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the building was last updated", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Unique identifier of the building", example = "1")
    private Long id;

    @Schema(description = "Name of the building", example = "Central Tower")
    private String name;

    @Schema(description = "Street address of the building", example = "123 Main Street")
    private String address;

    @Schema(description = "City where the building is located", example = "Madrid")
    private String city;
}
