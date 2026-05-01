package com.cookora.controller;

import com.cookora.dto.ApiResponse;
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

    @PostMapping("/{id}/review")
    public ApiResponse<String> addReview(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody ReviewRequestDTO request
    ) {
        reviewService.addOrUpdateReview(id, request);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Review added/updated successfully")
                .data(null)
                .build();
    }

    @GetMapping("/{id}/reviews")
    public ApiResponse<List<ReviewResponseDTO>> getReviews(
            @PathVariable Long id,
            @RequestParam(defaultValue = "latest") String sortBy
    ) {
        return ApiResponse.<List<ReviewResponseDTO>>builder()
                .success(true)
                .message("Reviews fetched successfully")
                .data(reviewService.getReviews(id, sortBy))
                .build();
    }
}