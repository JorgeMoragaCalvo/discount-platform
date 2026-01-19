package com.mygroup.discountplatform.controllers;

import com.mygroup.discountplatform.dtos.BuildingByCityDTO;
import com.mygroup.discountplatform.dtos.BuildingCreateRequestDTO;
import com.mygroup.discountplatform.dtos.BuildingListDTO;
import com.mygroup.discountplatform.dtos.BuildingResponseDTO;
import com.mygroup.discountplatform.services.BuildingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buildings")
@Tag(name = "Building", description = "Building management APIs")
public class BuildingController {

    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    /**
     * Finds buildings, optionally filtered by city name
     */
    @Operation(
        summary = "Get all buildings or filter by city",
        description = "Retrieves a list of all buildings in the system. Optionally filter by city using the 'city' query parameter."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved buildings"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<?> findAll(
        @Parameter(description = "Optional city name to filter buildings")
        @RequestParam(required = false) String city
    ) {
        if (city != null && !city.isBlank()) {
            List<BuildingByCityDTO> buildings = buildingService.findByCity(city);
            return new ResponseEntity<>(buildings, HttpStatus.OK);
        }
        List<BuildingListDTO> buildings = buildingService.findAll();
        return new ResponseEntity<>(buildings, HttpStatus.OK);
    }

    @Operation(
        summary = "Create a new building",
        description = "Creates a new building record in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Building created successfully"),
        @ApiResponse(responseCode = "400", description = "Validation error - missing or invalid required fields"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<BuildingResponseDTO> saveBuilding(
        @Valid @RequestBody BuildingCreateRequestDTO dto
    ) {
        BuildingResponseDTO building = buildingService.createBuilding(dto);
        return new ResponseEntity<>(building, HttpStatus.CREATED);
    }
}
