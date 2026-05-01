package com.cookora.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteRequestDTO {

    @NotNull(message = "Liked flag is required")
    private Boolean liked;
}