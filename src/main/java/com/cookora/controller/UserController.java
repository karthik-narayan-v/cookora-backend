package com.cookora.controller;

import com.cookora.dto.RecipeResponseDTO;
import com.cookora.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final FavoriteService favoriteService;

    @GetMapping("/{userId}/favorites")
    public List<RecipeResponseDTO> getFavorites(@PathVariable Long userId) {
        return favoriteService.getUserFavorites(userId);
    }
}
