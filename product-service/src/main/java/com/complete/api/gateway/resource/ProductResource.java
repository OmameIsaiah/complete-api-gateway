package com.complete.api.gateway.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "product resource", description = "Resource for managing products")
public class ProductResource {
    @GetMapping("/all")
    @Operation(
            summary = "Retrieve all products",
            description = "Resource to retrieve all products",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Products retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = List.class)
                            )
                    )
            }
    )
    @Tag(name = "product resource")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List> getAllProducts() {
        System.out.println("############## FETCHING ALL PRODUCTS...");
        return ResponseEntity.status(HttpStatus.OK).body(
                List.of("Rice", "Biscuit"));
    }
}
