package com.cookora.service;

import com.cookora.dto.ReviewRequestDTO;
import com.cookora.dto.ReviewResponseDTO;
import com.cookora.entity.Recipe;
import com.cookora.entity.Review;
import com.cookora.exception.ResourceNotFoundException;
import com.cookora.mapper.ReviewMapper;
import com.cookora.repository.RecipeRepository;
import com.cookora.repository.ReviewRepository;
import com.cookora.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RecipeRepository recipeRepository;

    // ⭐ Add or Update Review
    @Transactional
    public void addOrUpdateReview(Long recipeId, ReviewRequestDTO dto) {

        String userId = AuthUtil.getUserId();

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        Review review = reviewRepository
                .findByUserIdAndRecipe(userId, recipe)
                .orElseGet(() -> Review.builder()
                        .userId(userId)
                        .recipe(recipe)
                        .createdAt(LocalDateTime.now())
                        .build()
                );

        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        reviewRepository.save(review);

        updateRecipeRating(recipe);
    }

    // ⭐ Get Reviews for a Recipe
    public List<ReviewResponseDTO> getReviews(Long recipeId) {

        // validate recipe existence
        if (!recipeRepository.existsById(recipeId)) {
            throw new ResourceNotFoundException("Recipe not found");
        }

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