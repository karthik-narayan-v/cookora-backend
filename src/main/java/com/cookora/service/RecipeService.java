package com.cookora.service;

import com.cookora.dto.PagedResponseDTO;
import com.cookora.dto.RecipeFilterDTO;
import com.cookora.dto.RecipeRequestDTO;
import com.cookora.dto.RecipeResponseDTO;
import com.cookora.entity.Favorite;
import com.cookora.entity.MealType;
import com.cookora.entity.Recipe;
import com.cookora.entity.Tag;
import com.cookora.mapper.RecipeMapper;
import com.cookora.repository.FavoriteRepository;
import com.cookora.repository.MealTypeRepository;
import com.cookora.repository.RecipeRepository;
import com.cookora.repository.TagRepository;
import com.cookora.specification.RecipeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MealTypeRepository mealTypeRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    public Page<Recipe> getAllRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public RecipeResponseDTO createRecipe(RecipeRequestDTO dto) {

        Recipe recipe = RecipeMapper.toEntity(dto);

        Recipe saved = recipeRepository.save(recipe);

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

        return RecipeMapper.toDTO(saved);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public PagedResponseDTO<List<RecipeResponseDTO>> getFilteredRecipes(
            RecipeFilterDTO filter,
            Pageable pageable
    ) {

        Page<Recipe> recipes = recipeRepository.findAll(
                RecipeSpecification.withFilters(filter),
                pageable
        );

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

    public PagedResponseDTO<List<RecipeResponseDTO>> searchRecipes(
            String query,
            Pageable pageable
    ) {

        if (query == null || query.trim().isEmpty()) {
            return getFilteredRecipes(new RecipeFilterDTO(), pageable);
        }

        Page<Recipe> recipes =
                recipeRepository.searchRecipes(query, pageable);

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

    public PagedResponseDTO<List<RecipeResponseDTO>> getTrendingRecipes(Pageable pageable) {

        Page<Recipe> recipes = recipeRepository.findTrendingRecipes(pageable);

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

    public PagedResponseDTO<List<RecipeResponseDTO>> getRecommendations(
            Pageable pageable
    ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

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