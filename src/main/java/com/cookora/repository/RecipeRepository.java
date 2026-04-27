package com.cookora.repository;

import com.cookora.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface RecipeRepository extends
        JpaRepository<Recipe, Long>,
        JpaSpecificationExecutor<Recipe> {
}