package com.cookora.controller;

import com.cookora.dto.CollectionRequestDTO;
import com.cookora.dto.CollectionResponseDTO;
import com.cookora.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    // ⭐ Create
    @PostMapping
    public CollectionResponseDTO create(
            @RequestBody CollectionRequestDTO request
    ) {
        return collectionService.createCollection(request.getName());
    }

    // ⭐ Get All
    @GetMapping
    public List<CollectionResponseDTO> getAll() {
        return collectionService.getUserCollections();
    }

    // ⭐ Add recipe
    @PostMapping("/{id}/recipes/{recipeId}")
    public void addRecipe(
            @PathVariable Long id,
            @PathVariable Long recipeId
    ) {
        collectionService.addRecipe(id, recipeId);
    }

    // ⭐ Remove recipe
    @DeleteMapping("/{id}/recipes/{recipeId}")
    public void removeRecipe(
            @PathVariable Long id,
            @PathVariable Long recipeId
    ) {
        collectionService.removeRecipe(id, recipeId);
    }
}
