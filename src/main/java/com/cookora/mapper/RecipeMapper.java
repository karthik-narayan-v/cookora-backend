package com.cookora.mapper;

import com.cookora.dto.RecipeRequestDTO;
import com.cookora.dto.RecipeResponseDTO;
import com.cookora.entity.Ingredient;
import com.cookora.entity.Instruction;
import com.cookora.entity.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeMapper {

    public static RecipeResponseDTO toDTO(Recipe recipe) {

        List<String> ingredients = recipe.getIngredients()
                .stream()
                .map(Ingredient::getName)
                .toList();

        List<String> instructions = recipe.getInstructions()
                .stream()
                .map(Instruction::getDescription)
                .toList();

        return RecipeResponseDTO.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .difficulty(recipe.getDifficulty())
                .cuisine(recipe.getCuisine())
                .caloriesPerServing(recipe.getCaloriesPerServing())
                .prepTimeMinutes(recipe.getPrepTimeMinutes())
                .cookTimeMinutes(recipe.getCookTimeMinutes())
                .servings(recipe.getServings())
                .image(recipe.getImage())
                .ingredients(ingredients)
                .instructions(instructions)
                .build();
    }

    public static Recipe toEntity(RecipeRequestDTO dto) {

        Recipe recipe = Recipe.builder()
                .name(dto.getName())
                .difficulty(dto.getDifficulty())
                .cuisine(dto.getCuisine())
                .caloriesPerServing(dto.getCaloriesPerServing())
                .prepTimeMinutes(dto.getPrepTimeMinutes())
                .cookTimeMinutes(dto.getCookTimeMinutes())
                .servings(dto.getServings())
                .image(dto.getImage())
                .build();

        // Ingredients
        List<Ingredient> ingredients = dto.getIngredients().stream()
                .map(name -> Ingredient.builder()
                        .name(name)
                        .recipe(recipe)
                        .build())
                .toList();

        // Instructions
        List<Instruction> instructions = new ArrayList<>();
        for (int i = 0; i < dto.getInstructions().size(); i++) {
            instructions.add(
                    Instruction.builder()
                            .stepNumber(i + 1)
                            .description(dto.getInstructions().get(i))
                            .recipe(recipe)
                            .build()
            );
        }

        recipe.setIngredients(ingredients);
        recipe.setInstructions(instructions);

        return recipe;
    }
}