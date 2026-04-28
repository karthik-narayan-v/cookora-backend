package com.cookora.mapper;

import com.cookora.dto.ReviewResponseDTO;
import com.cookora.entity.Review;

public class ReviewMapper {

    public static ReviewResponseDTO toDTO(Review review) {
        return ReviewResponseDTO.builder()
                .userId(review.getUserId())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
    }
}