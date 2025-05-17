package com.example.backend.controller;

import com.example.backend.dto.Response;
import com.example.backend.entity.Users.Users;
import com.example.backend.repository.Users.UsersRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/")
public class MeHandler {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/me")
    public Response getUserName(HttpServletRequest request) {
        // 从Cookie中提取Token
        String token = extractToken(request);
        System.out.println("Extracted Token: " + token);
        Users user = null;
        if (token != null) {
            System.out.println("Searching for user by Token...");
            user = usersRepository.findByToken(token);
            System.out.println("User found: " + user);
            // 验证Token有效性及过期时间
            if (user != null && user.getTokenExpiry().isAfter(LocalDateTime.now())) {
                return new Response(0, user.getName());
            }
            System.out.println("User not found: ");
        }

        return new Response(1, null);
    }
    private String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}