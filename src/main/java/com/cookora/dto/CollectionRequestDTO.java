package com.cookora.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionRequestDTO {

    @NotBlank(message = "Collection name is required")
    @Size(max = 100, message = "Collection name cannot exceed 100 characters")
    private String name;
}