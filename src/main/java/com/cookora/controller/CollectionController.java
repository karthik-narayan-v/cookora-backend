package com.cookora.controller;

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

    // ⭐ Create Collection
    @PostMapping
    public CollectionResponseDTO create(
            @Valid @RequestBody CollectionRequestDTO request
    ) {
        return collectionService.createCollection(request.getName());
    }

    // ⭐ Get All Collections (for logged-in user)
    @GetMapping
    public List<CollectionResponseDTO> getAll() {
        return collectionService.getUserCollections();
    }

    @PostMapping("/{id}/recipes/{recipeId}")
    public void addRecipe(
            @PathVariable @Min(value = 1, message = "Collection ID must be valid") Long id,
            @PathVariable @Min(value = 1, message = "Recipe ID must be valid") Long recipeId
    ) {
        collectionService.addRecipe(id, recipeId);
    }

    // ⭐ Remove recipe from collection
    @DeleteMapping("/{id}/recipes/{recipeId}")
    public void removeRecipe(
            @PathVariable @Min(value = 1, message = "Collection ID must be valid") Long id,
            @PathVariable @Min(value = 1, message = "Recipe ID must be valid") Long recipeId
    ) {
        collectionService.removeRecipe(id, recipeId);
    }
}