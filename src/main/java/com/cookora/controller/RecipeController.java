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
    public ApiResponse<RecipeResponseDTO> getRecipe(
            @PathVariable @Min(value = 1, message = "Recipe ID must be valid") Long id
    ) {
        return ApiResponse.<RecipeResponseDTO>builder()
                .success(true)
                .message("Recipe fetched successfully")
                .data(recipeService.getRecipeById(id))
                .build();
    }

    // ⭐ Create Recipe
    @PostMapping
    public ApiResponse<RecipeResponseDTO> createRecipe(
            @Valid @RequestBody RecipeRequestDTO dto
    ) {
        return ApiResponse.<RecipeResponseDTO>builder()
                .success(true)
                .message("Recipe created successfully")
                .data(recipeService.createRecipe(dto))
                .build();
    }

    // ⭐ Delete Recipe
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteRecipe(
            @PathVariable @Min(value = 1, message = "Recipe ID must be valid") Long id
    ) {
        recipeService.deleteRecipe(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Recipe deleted successfully")
                .data(null)
                .build();
    }

    // ⭐ Get Recipes (Filter + Sort + Pagination)
    @GetMapping
    public ApiResponse<PagedResponseDTO<List<RecipeResponseDTO>>> getRecipes(
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

        return ApiResponse.<PagedResponseDTO<List<RecipeResponseDTO>>>builder()
                .success(true)
                .message("Recipes fetched successfully")
                .data(recipeService.getFilteredRecipes(filter, sortedPageable))
                .build();
    }

    // ⭐ Favorite / Unfavorite
    @PostMapping("/{id}/favorite")
    public ApiResponse<String> markFavorite(
            @PathVariable @Min(value = 1, message = "Recipe ID must be valid") Long id,
            @Valid @RequestBody FavoriteRequestDTO request
    ) {
        favoriteService.markFavorite(id, request.getLiked());

        return ApiResponse.<String>builder()
                .success(true)
                .message("Favorite updated successfully")
                .data(null)
                .build();
    }

    // ⭐ Search
    @GetMapping("/search")
    public ApiResponse<PagedResponseDTO<List<RecipeResponseDTO>>> searchRecipes(
            @RequestParam String query,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            Pageable pageable
    ) {
        return ApiResponse.<PagedResponseDTO<List<RecipeResponseDTO>>>builder()
                .success(true)
                .message("Search results fetched successfully")
                .data(recipeService.searchRecipes(query, sortBy, direction, pageable))
                .build();
    }

    // ⭐ Trending
    @GetMapping("/trending")
    public ApiResponse<PagedResponseDTO<List<RecipeResponseDTO>>> getTrendingRecipes(
            Pageable pageable
    ) {
        return ApiResponse.<PagedResponseDTO<List<RecipeResponseDTO>>>builder()
                .success(true)
                .message("Trending recipes fetched successfully")
                .data(recipeService.getTrendingRecipes(pageable))
                .build();
    }

    // ⭐ Recommendations
    @GetMapping("/recommendations")
    public ApiResponse<PagedResponseDTO<List<RecipeResponseDTO>>> getRecommendations(
            Pageable pageable
    ) {
        return ApiResponse.<PagedResponseDTO<List<RecipeResponseDTO>>>builder()
                .success(true)
                .message("Recommendations fetched successfully")
                .data(recipeService.getRecommendations(pageable))
                .build();
    }
}