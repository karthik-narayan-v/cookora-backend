package com.cookora.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
public class RecipeRequestDTO {

    private String name;
    private String difficulty;
    private String cuisine;
    private int caloriesPerServing;

    private int prepTimeMinutes;
    private int cookTimeMinutes;
    private int servings;

    private String image;

    private List<String> ingredients;
    private List<String> instructions;
}