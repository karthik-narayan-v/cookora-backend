package com.cookora.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CollectionResponseDTO {

    private Long id;
    private String name;
    private List<RecipeResponseDTO> recipes;
}