package com.cookora.service;

import com.cookora.dto.CollectionResponseDTO;
import com.cookora.entity.Collection;
import com.cookora.entity.Recipe;
import com.cookora.exception.ResourceNotFoundException;
import com.cookora.mapper.CollectionMapper;
import com.cookora.repository.CollectionRepository;
import com.cookora.repository.RecipeRepository;
import com.cookora.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final RecipeRepository recipeRepository;

    // ⭐ Create Collection
    public CollectionResponseDTO createCollection(String name) {

        String userId = AuthUtil.getUserId();

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

        String userId = AuthUtil.getUserId();

        return collectionRepository.findByUserId(userId)
                .stream()
                .map(CollectionMapper::toDTO)
                .toList();
    }

    // ⭐ Add Recipe to Collection
    @Transactional
    public void addRecipe(Long collectionId, Long recipeId) {

        String userId = AuthUtil.getUserId();

        Collection collection = collectionRepository
                .findByIdAndUserId(collectionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        if (!collection.getRecipes().contains(recipe)) {
            collection.getRecipes().add(recipe);
        }
    }

    // ⭐ Remove Recipe
    @Transactional
    public void removeRecipe(Long collectionId, Long recipeId) {

        String userId = AuthUtil.getUserId();

        Collection collection = collectionRepository
                .findByIdAndUserId(collectionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found"));

        collection.getRecipes()
                .removeIf(r -> r.getId().equals(recipeId));

        collectionRepository.save(collection);
    }
}
