package com.cookora.mapper;

import com.cookora.dto.CollectionResponseDTO;
import com.cookora.entity.Collection;

public class CollectionMapper {

    public static CollectionResponseDTO toDTO(Collection collection) {
        return CollectionResponseDTO.builder()
                .id(collection.getId())
                .name(collection.getName())
                .recipes(
                        collection.getRecipes()
                                .stream()
                                .map(RecipeMapper::toDTO)
                                .toList()
                )
                .build();
    }
}
