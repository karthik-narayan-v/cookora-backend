package com.cookora.controller;

import com.cookora.dto.ApiResponse;
import com.cookora.dto.RecipeResponseDTO;
import com.cookora.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final FavoriteService favoriteService;

    @GetMapping("/favorites")
    public ApiResponse<List<RecipeResponseDTO>> getFavorites() {
        return ApiResponse.<List<RecipeResponseDTO>>builder()
                .success(true)
                .message("Favorites fetched successfully")
                .data(favoriteService.getUserFavorites())
                .build();
    }
}