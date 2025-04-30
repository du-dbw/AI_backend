package com.example.backend.controller;

import com.example.backend.dto.Users.LoginRequest;
import com.example.backend.dto.Users.RegisterRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Users.Users;
import com.example.backend.repository.Users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersHandler {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/findAll")
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest loginRequest) {
        String name = loginRequest.getName();
        String password = loginRequest.getPassword();

        Users user = usersRepository.findByName(name);

        if (user != null && user.getPassword().equals(password)) {
            return new Response(200, "Login successful");
        } else {
            return new Response(401, "Invalid username or password");
        }
    }

    @PostMapping("/register")
    public Response register(@RequestBody RegisterRequest registerRequest) {
        String name = registerRequest.getName();
        String password = registerRequest.getPassword();
        String email = registerRequest.getEmail();
        String phone = registerRequest.getPhone();

        // 检查用户名是否已存在
        if (usersRepository.existsByName(name)) {
            return new Response(400, "Username already exists");
        }

        // 创建新用户
        Users newUser = new Users();
        newUser.setName(name);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setPhone(phone);

        usersRepository.save(newUser);

        return new Response(200, "Registration successful");
    }
}