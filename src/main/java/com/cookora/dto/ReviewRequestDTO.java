package com.cookora.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {

    private int rating; // 1–5
    private String comment;
}