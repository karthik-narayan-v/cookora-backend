package com.cookora.controller;

import com.cookora.dto.*;
import com.cookora.entity.Recipe;
import com.cookora.service.FavoriteService;
import com.cookora.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.cookora.util.RecipeSortUtil.isValidSortField;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final FavoriteService favoriteService;

    @GetMapping("/{id}")
    public Recipe getRecipe(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

    @PostMapping
    public RecipeResponseDTO createRecipe(@RequestBody RecipeRequestDTO dto) {
        return recipeService.createRecipe(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }

    @GetMapping
    public PagedResponseDTO<List<RecipeResponseDTO>> getRecipes(
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Integer minCalories,
            @RequestParam(required = false) Integer maxCalories,
            @RequestParam(required = false, defaultValue = "rating") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String direction,
            Pageable pageable
    ) {

        if (!isValidSortField(sortBy)) {
            throw new RuntimeException("Invalid sort field");
        }

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
        );

        RecipeFilterDTO filter = new RecipeFilterDTO();
        filter.setDifficulty(difficulty);
        filter.setCuisine(cuisine);
        filter.setMinRating(minRating);
        filter.setMinCalories(minCalories);
        filter.setMaxCalories(maxCalories);

        return recipeService.getFilteredRecipes(filter, sortedPageable);
    }
    @PostMapping("/{id}/favorite")
    public void markFavorite(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestBody FavoriteRequestDTO request
    ) {
        favoriteService.markFavorite(id, userId, request.getLiked());
    }

    @GetMapping("/search")
    public PagedResponseDTO<List<RecipeResponseDTO>> searchRecipes(
            @RequestParam String query,
            Pageable pageable
    ) {
        return recipeService.searchRecipes(query, pageable);
    }

    @GetMapping("/trending")
    public PagedResponseDTO<List<RecipeResponseDTO>> getTrendingRecipes(Pageable pageable) {
        return recipeService.getTrendingRecipes(pageable);
    }

}