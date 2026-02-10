package com.mygroup.discountplatform.controllers;

import com.mygroup.discountplatform.dtos.ClientCreateRequestDTO;
import com.mygroup.discountplatform.dtos.ClientListDTO;
import com.mygroup.discountplatform.dtos.ClientResponseDTO;
import com.mygroup.discountplatform.services.ClientService;
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
@RequestMapping("/clients")
@Tag(name = "Client", description = "Client management APIs")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(
            summary = "Get all clients or filter by building",
            description = "Retrieves a list of all clients in the system. Optionally filter by building using the 'buildingId' query parameter."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved clients"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<ClientListDTO>> findAll(
            @Parameter(description = "Optional building ID to filter clients")
            @RequestParam(required = false) Long buildingId
    ) {
        if (buildingId != null) {
            List<ClientListDTO> clients = clientService.findByBuildingId(buildingId);
            return new ResponseEntity<>(clients, HttpStatus.OK);
        }
        List<ClientListDTO> clients = clientService.findAll();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @Operation(
            summary = "Get client by ID",
            description = "Retrieves detailed information about a specific client by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved client"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(
            @Parameter(description = "ID of the client to retrieve")
            @PathVariable Long id
    ) {
        ClientResponseDTO client = clientService.findById(id);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @Operation(
            summary = "Create a new client",
            description = "Creates a new client record in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error - missing or invalid required fields, duplicate email or RUT"),
            @ApiResponse(responseCode = "404", description = "Building not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(
            @Valid @RequestBody ClientCreateRequestDTO dto
    ) {
        ClientResponseDTO client = clientService.createClient(dto);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }
}
