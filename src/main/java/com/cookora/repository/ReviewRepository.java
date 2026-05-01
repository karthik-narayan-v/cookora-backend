package com.cookora.repository;

import com.cookora.entity.Review;
import com.cookora.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserIdAndRecipe(String userId, Recipe recipe);

    List<Review> findByRecipe(Recipe recipe);

    List<Review> findByRecipeId(Long recipeId);

    List<Review> findByRecipeIdOrderByCreatedAtDesc(Long recipeId);

    List<Review> findByRecipeIdOrderByRatingDesc(Long recipeId);
}