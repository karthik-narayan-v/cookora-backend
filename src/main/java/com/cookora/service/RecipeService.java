package com.cookora.service;

import com.cookora.dto.RecipeFilterDTO;
import com.cookora.entity.Recipe;
import com.cookora.repository.RecipeRepository;
import com.cookora.specification.RecipeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public Page<Recipe> getAllRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public Page<Recipe> getFilteredRecipes(RecipeFilterDTO filter, Pageable pageable) {
        return recipeRepository.findAll(
                RecipeSpecification.withFilters(filter),
                pageable
        );
    }
}