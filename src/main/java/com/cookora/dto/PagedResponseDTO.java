package com.cookora.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PagedResponseDTO<T> {

    private T data;
    private int page;
    private int size;
    private long total;
}