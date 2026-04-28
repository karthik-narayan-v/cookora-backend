package com.cookora.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {

    private Long userId;
    private int rating;
    private String comment;
}