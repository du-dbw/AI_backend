package com.example.backend.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.Cookie;

public class TokenExtractInterceptor implements HandlerInterceptor {
    //@Override
    public static String extractToken(HttpServletRequest request) {
        return "f2805f61-855b-43d6-927a-de49daeed16f";
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("userToken".equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
    }
}