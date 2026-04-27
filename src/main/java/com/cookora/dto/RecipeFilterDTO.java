package com.cookora.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeFilterDTO {
    private String difficulty;
    private String cuisine;
    private Double minRating;
    private Integer minCalories;
    private Integer maxCalories;
}
