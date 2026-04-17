package com.example.resortbackendapplication1.controller;

import com.example.resortbackendapplication1.commons.dto.request.PaginatedRequest;
import com.example.resortbackendapplication1.commons.dto.response.PaginatedResponse;
import com.example.resortbackendapplication1.commons.dto.response.SuccessResponse;
import com.example.resortbackendapplication1.dto.request.pricetypes.CreatePriceTypeRequest;
import com.example.resortbackendapplication1.dto.request.pricetypes.UpdatePriceTypeRequest;
import com.example.resortbackendapplication1.dto.response.pricetypes.PriceTypeResponse;
import com.example.resortbackendapplication1.service.PriceTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Price Types", description = "Manage price types used to categorize room pricing periods")
@RestController
@RequestMapping("/api/v1/price-types")
public class PriceTypeController {

    private final PriceTypeService priceTypeService;

    public PriceTypeController(PriceTypeService priceTypeService) {
        this.priceTypeService = priceTypeService;
    }

    @Operation(summary = "Create a price type", description = "Creates a new price type with a unique code, name, and description.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Price type created successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> createPriceType(@RequestBody CreatePriceTypeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(priceTypeService.createPriceType(request));
    }

    @Operation(summary = "Get a price type by ID", description = "Returns a single price type by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Price type found",
                    content = @Content(schema = @Schema(implementation = PriceTypeResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Price type not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getPriceType(
            @Parameter(description = "ID of the price type") @PathVariable Long id) {
        return ResponseEntity.ok(priceTypeService.getPriceType(id));
    }

    @Operation(summary = "List all price types", description = "Returns a paginated list of all active price types. Sortable by: id, code, name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned successfully",
                    content = @Content(schema = @Schema(implementation = PaginatedResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping
    public ResponseEntity<?> getAllPriceTypes(@ParameterObject PaginatedRequest request) {
        Pageable pageable = request.toPageable(Set.of("id", "code", "name"));
        return ResponseEntity.ok(priceTypeService.getAllPriceTypes(pageable));
    }

    @Operation(summary = "Update a price type", description = "Partially updates an existing price type. Only provided fields are updated.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Price type updated successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Price type not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePriceType(
            @Parameter(description = "ID of the price type to update") @PathVariable Long id,
            @RequestBody UpdatePriceTypeRequest request) {
        return ResponseEntity.ok(priceTypeService.updatePriceType(id, request));
    }

    @Operation(summary = "Delete a price type", description = "Soft-deletes a price type by marking it as inactive and deleted.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Price type deleted successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Price type not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePriceType(
            @Parameter(description = "ID of the price type to delete") @PathVariable Long id) {
        return ResponseEntity.ok(priceTypeService.deletePriceType(id));
    }
}
