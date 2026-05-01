package com.cookora.controller;

import com.cookora.dto.ReviewRequestDTO;
import com.cookora.dto.ReviewResponseDTO;
import com.cookora.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ⭐ Add / Update Review
    @PostMapping("/{id}/review")
    public void addReview(
            @PathVariable @Min(value = 1, message = "Recipe ID must be valid") Long id,
            @Valid @RequestBody ReviewRequestDTO request
    ) {
        reviewService.addOrUpdateReview(id, request);
    }

    // ⭐ Get Reviews
    @GetMapping("/{id}/reviews")
    public List<ReviewResponseDTO> getReviews(
            @PathVariable Long id,
            @RequestParam(defaultValue = "latest") String sortBy
    ) {
        return reviewService.getReviews(id, sortBy);
    }
}