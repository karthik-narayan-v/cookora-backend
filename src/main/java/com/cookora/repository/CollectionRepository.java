package com.cookora.repository;

import com.cookora.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findByUserId(String userId);

    Optional<Collection> findByIdAndUserId(Long id, String userId);
}