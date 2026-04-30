package com.cookora.repository;

import com.cookora.entity.Favorite;
import com.cookora.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserIdAndRecipe(String userId, Recipe recipe);

    List<Favorite> findByUserIdAndLikedTrue(String userId);
}