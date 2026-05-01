package com.cookora.controller;

import com.cookora.dto.ApiResponse;
import com.cookora.dto.CollectionRequestDTO;
import com.cookora.dto.CollectionResponseDTO;
import com.cookora.service.CollectionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public ApiResponse<CollectionResponseDTO> create(
            @Valid @RequestBody CollectionRequestDTO request
    ) {
        return ApiResponse.<CollectionResponseDTO>builder()
                .success(true)
                .message("Collection created successfully")
                .data(collectionService.createCollection(request.getName()))
                .build();
    }

    @GetMapping
    public ApiResponse<List<CollectionResponseDTO>> getAll() {
        return ApiResponse.<List<CollectionResponseDTO>>builder()
                .success(true)
                .message("Collections fetched successfully")
                .data(collectionService.getUserCollections())
                .build();
    }

    @PostMapping("/{id}/recipes/{recipeId}")
    public ApiResponse<String> addRecipe(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long recipeId
    ) {
        collectionService.addRecipe(id, recipeId);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Recipe added to collection")
                .data(null)
                .build();
    }

    @DeleteMapping("/{id}/recipes/{recipeId}")
    public ApiResponse<String> removeRecipe(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long recipeId
    ) {
        collectionService.removeRecipe(id, recipeId);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Recipe removed from collection")
                .data(null)
                .build();
    }
}