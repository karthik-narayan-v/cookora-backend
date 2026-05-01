package com.cookora.controller;

import com.cookora.dto.*;
import com.cookora.exception.BadRequestException;
import com.cookora.service.FavoriteService;
import com.cookora.service.RecipeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.cookora.util.RecipeSortUtil.isValidSortField;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final FavoriteService favoriteService;

    // ⭐ Get Recipe
    @GetMapping("/{id}")
    public RecipeResponseDTO getRecipe(
            @PathVariable @Min(value = 1, message = "Recipe ID must be valid") Long id
    ) {
        return recipeService.getRecipeById(id);
    }

    // ⭐ Create Recipe
    @PostMapping
    public RecipeResponseDTO createRecipe(
            @Valid @RequestBody RecipeRequestDTO dto
    ) {
        return recipeService.createRecipe(dto);
    }

    // ⭐ Delete Recipe
    @DeleteMapping("/{id}")
    public void deleteRecipe(
            @PathVariable @Min(value = 1, message = "Recipe ID must be valid") Long id
    ) {
        recipeService.deleteRecipe(id);
    }

    // ⭐ Get Recipes (Filter + Sort + Pagination)
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
            throw new BadRequestException("Invalid sort field");
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

    // ⭐ Favorite / Unfavorite
    @PostMapping("/{id}/favorite")
    public void markFavorite(
            @PathVariable @Min(value = 1, message = "Recipe ID must be valid") Long id,
            @Valid @RequestBody FavoriteRequestDTO request
    ) {
        favoriteService.markFavorite(id, request.getLiked());
    }

    // ⭐ Search
    @GetMapping("/search")
    public PagedResponseDTO<List<RecipeResponseDTO>> searchRecipes(
            @RequestParam String query,
            Pageable pageable
    ) {
        return recipeService.searchRecipes(query, pageable);
    }

    // ⭐ Trending
    @GetMapping("/trending")
    public PagedResponseDTO<List<RecipeResponseDTO>> getTrendingRecipes(
            Pageable pageable
    ) {
        return recipeService.getTrendingRecipes(pageable);
    }

    // ⭐ Recommendations
    @GetMapping("/recommendations")
    public PagedResponseDTO<List<RecipeResponseDTO>> getRecommendations(
            Pageable pageable
    ) {
        return recipeService.getRecommendations(pageable);
    }
}