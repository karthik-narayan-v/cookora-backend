package com.cookora.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private boolean success;
    private String message;
    private String errorCode;
}