package com.mygroup.discountplatform.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "DTO for listing buildings")
public class BuildingListDTO {

    @Schema(description = "Name of the building", example = "Central Tower")
    private String name;

    @Schema(description = "Street address of the building", example = "123 Main Street")
    private String address;

    @Schema(description = "City where the building is located", example = "Santiago")
    private String city;
}
