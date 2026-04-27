package com.cookora.specification;

import com.cookora.dto.RecipeFilterDTO;
import com.cookora.entity.Recipe;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecification {

    public static Specification<Recipe> withFilters(RecipeFilterDTO filter) {
        return (root, query, cb) -> {

            var predicates = cb.conjunction();

            if (filter.getDifficulty() != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("difficulty"), filter.getDifficulty()));
            }

            if (filter.getCuisine() != null) {
                predicates = cb.and(predicates,
                        cb.equal(root.get("cuisine"), filter.getCuisine()));
            }

            if (filter.getMinRating() != null) {
                predicates = cb.and(predicates,
                        cb.greaterThanOrEqualTo(root.get("rating"), filter.getMinRating()));
            }

            if (filter.getMinCalories() != null) {
                predicates = cb.and(predicates,
                        cb.greaterThanOrEqualTo(root.get("caloriesPerServing"), filter.getMinCalories()));
            }

            if (filter.getMaxCalories() != null) {
                predicates = cb.and(predicates,
                        cb.lessThanOrEqualTo(root.get("caloriesPerServing"), filter.getMaxCalories()));
            }

            return predicates;
        };
    }
}