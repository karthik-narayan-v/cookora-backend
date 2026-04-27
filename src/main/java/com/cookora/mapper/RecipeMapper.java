package com.cookora.mapper;

import com.cookora.dto.RecipeResponseDTO;
import com.cookora.entity.Recipe;

public class RecipeMapper {

    public static RecipeResponseDTO toDTO(Recipe recipe) {
        return RecipeResponseDTO.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .difficulty(recipe.getDifficulty())
                .cuisine(recipe.getCuisine())
                .caloriesPerServing(recipe.getCaloriesPerServing())
                .prepTimeMinutes(recipe.getPrepTimeMinutes())
                .cookTimeMinutes(recipe.getCookTimeMinutes())
                .servings(recipe.getServings())
                .rating(recipe.getRating())
                .reviewCount(recipe.getReviewCount())
                .image(recipe.getImage())
                .build();
    }
}