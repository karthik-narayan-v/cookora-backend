package com.cookora.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeRequestDTO {

    @NotBlank(message = "Recipe name is required")
    private String name;

    @NotBlank(message = "Difficulty is required")
    private String difficulty;

    @NotBlank(message = "Cuisine is required")
    private String cuisine;

    @Min(value = 1, message = "Calories must be at least 1")
    private int caloriesPerServing;

    @Min(value = 1, message = "Prep time must be at least 1 minute")
    private int prepTimeMinutes;

    @Min(value = 1, message = "Cook time must be at least 1 minute")
    private int cookTimeMinutes;

    @Min(value = 1, message = "Servings must be at least 1")
    private int servings;

    @NotBlank(message = "Image URL is required")
    private String image;

    @NotEmpty(message = "At least one ingredient is required")
    private List<@NotBlank(message = "Ingredient cannot be empty") String> ingredients;

    @NotEmpty(message = "At least one instruction is required")
    private List<@NotBlank(message = "Instruction cannot be empty") String> instructions;

    @NotEmpty(message = "At least one tag is required")
    private List<@NotBlank(message = "Tag cannot be empty") String> tags;

    @NotEmpty(message = "At least one meal type is required")
    private List<@NotBlank(message = "Meal type cannot be empty") String> mealType;
}