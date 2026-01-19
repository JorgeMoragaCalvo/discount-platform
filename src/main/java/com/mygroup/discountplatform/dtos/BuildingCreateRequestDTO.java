package com.mygroup.discountplatform.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Request DTO for creating a new building")
public class BuildingCreateRequestDTO {

    @NotBlank(message = "name is required")
    @Schema(description = "Name of the building", example = "Central Tower", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "address is required")
    @Schema(description = "Street address of the building", example = "123 Main Street", requiredMode = Schema.RequiredMode.REQUIRED)
    private String address;

    @NotBlank(message = "city is required")
    @Schema(description = "City where the building is located", example = "Madrid", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;
}
