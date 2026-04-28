package com.cookora.service;

import com.cookora.dto.RecipeResponseDTO;
import com.cookora.entity.Favorite;
import com.cookora.entity.Recipe;
import com.cookora.mapper.RecipeMapper;
import com.cookora.repository.FavoriteRepository;
import com.cookora.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    public void markFavorite(Long recipeId, Long userId, Boolean liked) {

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Optional<Favorite> existing =
                favoriteRepository.findByUserIdAndRecipe(userId, recipe);

        if (existing.isPresent()) {
            Favorite fav = existing.get();
            fav.setLiked(liked);
            favoriteRepository.save(fav);
        } else {
            Favorite fav = Favorite.builder()
                    .userId(userId)
                    .recipe(recipe)
                    .liked(liked)
                    .createdAt(LocalDateTime.now())
                    .build();

            favoriteRepository.save(fav);
        }
    }

    public List<RecipeResponseDTO> getUserFavorites(Long userId) {

        List<Favorite> favorites =
                favoriteRepository.findByUserIdAndLikedTrue(userId);

        return favorites.stream()
                .map(fav -> RecipeMapper.toDTO(fav.getRecipe()))
                .toList();
    }
}
