package com.example.backend.controller;

import com.example.backend.dto.Users.EditBioRequest;
import com.example.backend.dto.Users.LoginRequest;
import com.example.backend.dto.Users.RegisterRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Users.Users;
import com.example.backend.repository.Users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // 上传头像的接口
//    @PostMapping("/upload-avatar/{userId}")
//    public Response uploadAvatar(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
//        // 这里应该有保存文件的逻辑，例如保存到服务器本地或云存储
//        // 并将文件的 URL 保存到数据库中的 avatar 字段
//        // 还没想明白到底存在哪里
//
//        // 假设文件已经成功保存，并得到了文件的 URL
//        String avatarUrl = "http://example.com/avatars/" + file.getOriginalFilename();
//
//        // 更新用户头像 URL
//        Users user = usersRepository.findById(userId).orElse(null);
//        if (user != null) {
//            user.setAvatar(avatarUrl);
//            usersRepository.save(user);
//            return new Response(200, "Avatar uploaded successfully");
//        } else {
//            return new Response(404, "User not found");
//        }
//    }
//
//    // 编辑个人简介的接口
//    @PutMapping("/edit-bio/{userId}")
//    public Response editBio(@PathVariable Long userId, @RequestBody EditBioRequest editBioRequest) {
//        String newBio = editBioRequest.getBio();
//
//        Users user = usersRepository.findById(userId).orElse(null);
//        if (user != null) {
//            user.setBio(newBio);
//            usersRepository.save(user);
//            return new Response(200, "Bio updated successfully");
//        } else {
//            return new Response(404, "User not found");
//        }
//    }

}