package com.cookora.controller;

import com.cookora.dto.ReviewRequestDTO;
import com.cookora.dto.ReviewResponseDTO;
import com.cookora.service.ReviewService;
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
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestBody ReviewRequestDTO request
    ) {
        reviewService.addOrUpdateReview(id, userId, request);
    }

    // ⭐ Get Reviews
    @GetMapping("/{id}/reviews")
    public List<ReviewResponseDTO> getReviews(@PathVariable Long id) {
        return reviewService.getReviews(id);
    }
}