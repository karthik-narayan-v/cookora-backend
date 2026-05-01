package com.cookora.service;

import com.cookora.dto.PagedResponseDTO;
import com.cookora.dto.RecipeFilterDTO;
import com.cookora.dto.RecipeRequestDTO;
import com.cookora.dto.RecipeResponseDTO;
import com.cookora.entity.*;
import com.cookora.exception.BadRequestException;
import com.cookora.exception.ResourceNotFoundException;
import com.cookora.exception.UnauthorizedException;
import com.cookora.mapper.RecipeMapper;
import com.cookora.repository.*;
import com.cookora.specification.RecipeSpecification;
import com.cookora.util.AuthUtil;
import com.cookora.util.RecipeSortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;
    private final MealTypeRepository mealTypeRepository;
    private final FavoriteRepository favoriteRepository;

    // ⭐ Get Recipe
    public RecipeResponseDTO getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .map(RecipeMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
    }

    // ⭐ Create Recipe
    @Transactional
    public RecipeResponseDTO createRecipe(RecipeRequestDTO dto) {

        String userId = AuthUtil.getUserId();

        Recipe recipe = RecipeMapper.toEntity(dto);
        recipe.setCreatedBy(userId);// 🔐 ownership

        // Tags
        List<Tag> tags = dto.getTags().stream()
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(
                                Tag.builder().name(name).build()
                        )))
                .toList();

        // Meal Types
        List<MealType> mealTypes = dto.getMealType().stream()
                .map(name -> mealTypeRepository.findByName(name)
                        .orElseGet(() -> mealTypeRepository.save(
                                MealType.builder().name(name).build()
                        )))
                .toList();

        recipe.setTags(tags);
        recipe.setMealTypes(mealTypes);

        Recipe saved = recipeRepository.save(recipe);

        return RecipeMapper.toDTO(saved);
    }

    // ⭐ Delete Recipe
    @Transactional
    public void deleteRecipe(Long recipeId) {

        String userId = AuthUtil.getUserId();

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        if (!recipe.getCreatedBy().equals(userId)) {
            throw new UnauthorizedException("You cannot delete this recipe");
        }

        recipeRepository.delete(recipe);
    }

    // ⭐ Filter Recipes
    public PagedResponseDTO<List<RecipeResponseDTO>> getFilteredRecipes(
            RecipeFilterDTO filter,
            Pageable pageable
    ) {

        Page<Recipe> recipes = recipeRepository.findAll(
                RecipeSpecification.withFilters(filter),
                pageable
        );

        return mapToPagedResponse(recipes);
    }

    // ⭐ Search Recipes
    public PagedResponseDTO<List<RecipeResponseDTO>> searchRecipes(
            String query,
            String sortBy,
            String direction,
            Pageable pageable
    ) {

        if (!RecipeSortUtil.isValidSortField(sortBy)) {
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

        Page<Recipe> recipes;

        if (query == null || query.trim().isEmpty()) {
            recipes = recipeRepository.findAll(sortedPageable);
        } else {
            recipes = recipeRepository.searchRecipes(query, sortedPageable);
        }

        return mapToPagedResponse(recipes);
    }

    // ⭐ Trending
    public PagedResponseDTO<List<RecipeResponseDTO>> getTrendingRecipes(Pageable pageable) {

        Page<Recipe> recipes = recipeRepository.findTrendingRecipes(pageable);

        return mapToPagedResponse(recipes);
    }

    // ⭐ Recommendations
    public PagedResponseDTO<List<RecipeResponseDTO>> getRecommendations(
            Pageable pageable
    ) {

        String userId = AuthUtil.getUserId();

        List<Favorite> favorites =
                favoriteRepository.findByUserIdAndLikedTrue(userId);

        List<String> cuisines = favorites.stream()
                .map(f -> f.getRecipe().getCuisine())
                .distinct()
                .toList();

        if (cuisines.isEmpty()) {
            return getTrendingRecipes(pageable);
        }

        Page<Recipe> recipes =
                recipeRepository.findRecommendedRecipes(cuisines, pageable);

        return mapToPagedResponse(recipes);
    }

    // ⭐ Common Mapper
    private PagedResponseDTO<List<RecipeResponseDTO>> mapToPagedResponse(Page<Recipe> recipes) {

        List<RecipeResponseDTO> dtoList =
                recipes.getContent().stream()
                        .map(RecipeMapper::toDTO)
                        .toList();

        return new PagedResponseDTO<>(
                dtoList,
                recipes.getNumber(),
                recipes.getSize(),
                recipes.getTotalElements()
        );
    }
}