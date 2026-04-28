package com.cookora.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "recipe_id"})
})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private int rating; // 1 to 5

    private String comment;

    private LocalDateTime createdAt;
}