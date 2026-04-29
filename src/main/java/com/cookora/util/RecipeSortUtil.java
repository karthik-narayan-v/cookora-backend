package com.cookora.util;

import java.util.Arrays;

public class RecipeSortUtil {

    public static boolean isValidSortField(String field) {
        return Arrays.stream(RecipeSortField.values())
                .anyMatch(f -> f.name().equals(field));
    }
}