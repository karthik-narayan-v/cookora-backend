package com.cookora.controller;

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

    // ⭐ Get logged-in user's favorites
    @GetMapping("/favorites")
    public List<RecipeResponseDTO> getFavorites() {
        return favoriteService.getUserFavorites();
    }
}