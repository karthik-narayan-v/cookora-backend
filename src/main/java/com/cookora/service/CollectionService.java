package com.cookora.service;

import com.cookora.dto.CollectionResponseDTO;
import com.cookora.entity.Collection;
import com.cookora.entity.Recipe;
import com.cookora.mapper.CollectionMapper;
import com.cookora.repository.CollectionRepository;
import com.cookora.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final RecipeRepository recipeRepository;

    // 🔐 get userId from JWT
    private String getUserId() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    // ⭐ Create Collection
    public CollectionResponseDTO createCollection(String name) {

        String userId = getUserId();

        Collection collection = Collection.builder()
                .userId(userId)
                .name(name)
                .recipes(new ArrayList<>())
                .build();

        return CollectionMapper.toDTO(
                collectionRepository.save(collection)
        );
    }

    // ⭐ Get All Collections
    public List<CollectionResponseDTO> getUserCollections() {

        String userId = getUserId();

        return collectionRepository.findByUserId(userId)
                .stream()
                .map(CollectionMapper::toDTO)
                .toList();
    }

    // ⭐ Add Recipe to Collection
    public void addRecipe(Long collectionId, Long recipeId) {

        String userId = getUserId();

        Collection collection = collectionRepository
                .findByIdAndUserId(collectionId, userId)
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        if (!collection.getRecipes().contains(recipe)) {
            collection.getRecipes().add(recipe);
        }

        collectionRepository.save(collection);
    }

    // ⭐ Remove Recipe
    public void removeRecipe(Long collectionId, Long recipeId) {

        String userId = getUserId();

        Collection collection = collectionRepository
                .findByIdAndUserId(collectionId, userId)
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        collection.getRecipes()
                .removeIf(r -> r.getId().equals(recipeId));

        collectionRepository.save(collection);
    }
}
