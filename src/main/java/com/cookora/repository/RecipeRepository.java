package com.cookora.repository;

import com.cookora.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface RecipeRepository extends
        JpaRepository<Recipe, Long>,
        JpaSpecificationExecutor<Recipe> {

    @Query("""
    SELECT DISTINCT r FROM Recipe r
    LEFT JOIN r.tags t
    WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(r.cuisine) LIKE LOWER(CONCAT('%', :query, '%'))
       OR LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%'))
""")
    Page<Recipe> searchRecipes(@Param("query") String query, Pageable pageable);
}