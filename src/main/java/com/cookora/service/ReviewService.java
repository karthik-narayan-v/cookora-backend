package com.cookora.service;

import com.cookora.dto.ReviewRequestDTO;
import com.cookora.dto.ReviewResponseDTO;
import com.cookora.entity.Recipe;
import com.cookora.entity.Review;
import com.cookora.mapper.ReviewMapper;
import com.cookora.repository.RecipeRepository;
import com.cookora.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RecipeRepository recipeRepository;

    // ⭐ Add or Update Review
    public void addOrUpdateReview(Long recipeId, String userId, ReviewRequestDTO dto) {

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Review review = reviewRepository
                .findByUserIdAndRecipe(userId, recipe)
                .orElse(null);

        if (review != null) {
            review.setRating(dto.getRating());
            review.setComment(dto.getComment());
        } else {
            review = Review.builder()
                    .userId(userId)
                    .recipe(recipe)
                    .rating(dto.getRating())
                    .comment(dto.getComment())
                    .createdAt(LocalDateTime.now())
                    .build();
        }

        reviewRepository.save(review);

        // 🔥 update rating after save
        updateRecipeRating(recipe);
    }

    // ⭐ Get Reviews for a Recipe
    public List<ReviewResponseDTO> getReviews(Long recipeId) {

        return reviewRepository.findByRecipeId(recipeId)
                .stream()
                .map(ReviewMapper::toDTO)
                .toList();
    }

    // ⭐ Internal: Update Recipe Rating
    private void updateRecipeRating(Recipe recipe) {

        List<Review> reviews = reviewRepository.findByRecipe(recipe);

        double avg = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);

        recipe.setRating(avg);
        recipe.setReviewCount(reviews.size());

        recipeRepository.save(recipe);
    }
}