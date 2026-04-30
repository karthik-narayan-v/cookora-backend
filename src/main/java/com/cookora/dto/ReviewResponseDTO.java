package com.cookora.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDTO {

    private String userId;
    private int rating;
    private String comment;
}