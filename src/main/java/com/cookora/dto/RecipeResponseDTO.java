package com.cookora.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponseDTO {

    private Long id;
    private String name;
    private String difficulty;
    private String cuisine;
    private int caloriesPerServing;
    private int prepTimeMinutes;
    private int cookTimeMinutes;
    private int servings;
    private double rating;
    private int reviewCount;
    private String image;
}