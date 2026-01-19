package com.mygroup.discountplatform.controllers;

import com.mygroup.discountplatform.entities.BuildingEntity;
import com.mygroup.discountplatform.services.BuildingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/building")
@Tag(name = "Building", description = "Building management APIs")
public class BuildingController {

    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @Operation(
        summary = "Get all buildings",
        description = "Retrieves a list of all buildings in the system"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all buildings")
    @GetMapping
    public ResponseEntity<List<BuildingEntity>> findAll(){
        return new ResponseEntity<>(buildingService.findAll(), HttpStatus.OK);
    }

    @Operation(
        summary = "Get buildings by city",
        description = "Retrieves all buildings located in the specified city"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved buildings for the city")
    @GetMapping("/{city}")
    public ResponseEntity<List<BuildingEntity>> findByCity(
        @Parameter(description = "Name of the city to filter buildings", required = true)
        @PathVariable String city
    ){
        return new ResponseEntity<>(buildingService.findByCity(city), HttpStatus.OK);
    }

    @Operation(
        summary = "Create a new building",
        description = "Creates a new building record in the system"
    )
    @ApiResponse(responseCode = "201", description = "Building created successfully")
    @PostMapping
    public ResponseEntity<BuildingEntity> saveBuilding(@RequestBody BuildingEntity buildingEntity){
        BuildingEntity building = buildingService.createBuilding(buildingEntity);
        return new ResponseEntity<>(building, HttpStatus.CREATED);
    }
}
