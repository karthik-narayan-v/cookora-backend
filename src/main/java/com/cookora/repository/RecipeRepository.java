package com.cookora.repository;

import com.cookora.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

@Repository
public interface RecipeRepository extends
        JpaRepository<Recipe, Long>,
        JpaSpecificationExecutor<Recipe> {

    @Query(value = """
SELECT DISTINCT r.* FROM recipe r
LEFT JOIN recipe_tags rt ON r.id = rt.recipe_id
LEFT JOIN tag t ON rt.tag_id = t.id
WHERE to_tsvector('english',
        COALESCE(r.name, '') || ' ' ||
        COALESCE(r.cuisine, '') || ' ' ||
        COALESCE(t.name, '')
      )
@@ websearch_to_tsquery('english', :query)
ORDER BY ts_rank(
        to_tsvector('english',
            COALESCE(r.name, '') || ' ' ||
            COALESCE(r.cuisine, '') || ' ' ||
            COALESCE(t.name, '')
        ),
        websearch_to_tsquery('english', :query)
) DESC
""",
            countQuery = """
SELECT COUNT(DISTINCT r.id) FROM recipe r
LEFT JOIN recipe_tags rt ON r.id = rt.recipe_id
LEFT JOIN tag t ON rt.tag_id = t.id
WHERE to_tsvector('english',
        COALESCE(r.name, '') || ' ' ||
        COALESCE(r.cuisine, '') || ' ' ||
        COALESCE(t.name, '')
      )
@@ websearch_to_tsquery('english', :query)
""",
            nativeQuery = true)
    Page<Recipe> searchRecipes(@Param("query") String query, Pageable pageable);

    @Query("""
SELECT r FROM Recipe r
LEFT JOIN Favorite f ON f.recipe = r AND f.liked = true
GROUP BY r
ORDER BY (r.rating * r.reviewCount * 0.5 + COUNT(f) * 1.5) DESC
""")
    Page<Recipe> findTrendingRecipes(Pageable pageable);

    @Query("""
SELECT DISTINCT r FROM Recipe r
WHERE r.cuisine IN :cuisines
ORDER BY r.rating DESC
""")
    Page<Recipe> findRecommendedRecipes(
            @Param("cuisines") List<String> cuisines,
            Pageable pageable
    );

    @Query("""
SELECT DISTINCT r FROM Recipe r
JOIN r.tags t
WHERE r.cuisine IN :cuisines
   OR t.name IN :tags
   OR r.caloriesPerServing BETWEEN :minCal AND :maxCal
ORDER BY r.rating DESC
""")
    Page<Recipe> findAdvancedRecommendations(
            List<String> cuisines,
            List<String> tags,
            int minCal,
            int maxCal,
            Pageable pageable
    );

    @Query("""
SELECT r FROM Recipe r
JOIN Favorite f ON f.recipe = r
WHERE f.userId IN (
    SELECT f2.userId FROM Favorite f2
    WHERE f2.recipe.id IN :favoriteRecipeIds
    AND f2.liked = true
)
AND r.id NOT IN :favoriteRecipeIds
ORDER BY r.rating DESC
""")
    Page<Recipe> findCollaborativeRecommendations(
            List<Long> favoriteRecipeIds,
            Pageable pageable
    );
}