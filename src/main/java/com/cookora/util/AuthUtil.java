package com.cookora.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    public static String getUserId() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}