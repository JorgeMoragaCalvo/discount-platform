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
@Schema(description = "DTO for profession information")
public class ProfessionDTO {

    @Schema(description = "Unique identifier of the profession", example = "1")
    private Long id;

    @Schema(description = "Name of the profession", example = "Engineer")
    private String name;
}
