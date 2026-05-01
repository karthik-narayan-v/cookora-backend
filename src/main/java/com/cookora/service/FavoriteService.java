package com.cookora.service;

import com.cookora.dto.RecipeResponseDTO;
import com.cookora.entity.Favorite;
import com.cookora.entity.Recipe;
import com.cookora.exception.ResourceNotFoundException;
import com.cookora.mapper.RecipeMapper;
import com.cookora.repository.FavoriteRepository;
import com.cookora.repository.RecipeRepository;
import com.cookora.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final RecipeRepository recipeRepository;

    // ⭐ Add / Update Favorite
    @Transactional
    public void markFavorite(Long recipeId, Boolean liked) {

        String userId = AuthUtil.getUserId();

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        Favorite favorite = favoriteRepository
                .findByUserIdAndRecipe(userId, recipe)
                .orElseGet(() -> Favorite.builder()
                        .userId(userId)
                        .recipe(recipe)
                        .createdAt(LocalDateTime.now())
                        .build()
                );

        favorite.setLiked(liked);

        favoriteRepository.save(favorite);
    }

    // ⭐ Get Logged-in User Favorites
    public List<RecipeResponseDTO> getUserFavorites() {

        String userId = AuthUtil.getUserId();

        return favoriteRepository.findByUserIdAndLikedTrue(userId)
                .stream()
                .map(fav -> RecipeMapper.toDTO(fav.getRecipe()))
                .toList();
    }
}