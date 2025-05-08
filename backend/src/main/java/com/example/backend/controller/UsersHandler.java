package com.example.backend.controller;

import com.example.backend.dto.Users.EditBioRequest;
import com.example.backend.dto.Users.LoginRequest;
import com.example.backend.dto.Users.RegisterRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Users.Users;
import com.example.backend.repository.Users.UsersRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.Cookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UsersHandler {

    @Autowired
    private UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/findAll")
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

//    @PostMapping("/login")
//    public Response login(@RequestBody LoginRequest loginRequest , HttpServletResponse response) {
//       // System.out.println("login!!!!!!");
//        String name = loginRequest.getName();
//        String password = loginRequest.getPassword();
//
//        Users user = usersRepository.findByName(name);
//
//        if (user != null && user.getPassword().equals(password)) {
//            // 添加Cookie
//            Cookie cookie = new Cookie("userToken", String.valueOf(user.getId()));
//            cookie.setMaxAge(3600);  // 设置Cookie的过期时间为1小时
//            cookie.setPath("/");
//            response.addCookie(cookie);
//
//            return new Response(200, "Login successful");
//        } else {
//            return new Response(401, "Invalid username or password");
//        }
//    }
//
//    @PostMapping("/register")
//    public Response register(@RequestBody RegisterRequest registerRequest) {
//        String name = registerRequest.getName();
//        String password = registerRequest.getPassword();
//        String email = registerRequest.getEmail();
//        String phone = registerRequest.getPhone();
//
//        // 检查用户名是否已存在
//        if (usersRepository.existsByName(name)) {
//            return new Response(400, "Username already exists");
//        }
//
//        // 检查邮箱是否已存在
//        if (usersRepository.existsByEmail(email)) {
//            return new Response(400, "Email already exists");
//        }
//        // 创建新用户
//        Users newUser = new Users();
//        newUser.setName(name);
//        newUser.setPassword(password);
//        newUser.setEmail(email);
//        newUser.setPhone(phone);
//
//        usersRepository.save(newUser);
//
//        return new Response(200, "Registration successful");
//    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String name = loginRequest.getName();
        String password = loginRequest.getPassword();

        Users user = usersRepository.findByName(name);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            Cookie cookie = new Cookie("userToken", String.valueOf(user.getId()));
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            response.addCookie(cookie);

            return new Response(200, "Login successful");
        } else {
            return new Response(401, "Invalid username or password");
        }
    }

    @PostMapping("/register")
    public Response register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
        // 处理验证错误
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return new Response(400, errorMessage);
        }

        String name = registerRequest.getName();
        String password = registerRequest.getPassword();
        String email = registerRequest.getEmail();
        String phone = registerRequest.getPhone();

        if (usersRepository.existsByName(name)) {
            return new Response(400, "Username already exists");
        }

        if (usersRepository.existsByEmail(email)) {
            return new Response(400, "Email already exists");
        }

        Users newUser = new Users();
        newUser.setName(name);
        newUser.setPassword(passwordEncoder.encode(password)); // 加密密码
        newUser.setEmail(email);
        newUser.setPhone(phone);

        usersRepository.save(newUser);

        return new Response(200, "Registration successful");
    }



    // 上传头像的接口
    @PostMapping("/upload-avatar/{userId}")
    public ResponseEntity<Response> uploadAvatar(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        // 检查用户是否存在
        Users user = usersRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response(404, "User not found"));
        }

        // 检查文件是否为空
        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400, "File is empty"));
        }

        // 检查文件类型是否为图片
        String contentType = file.getContentType();
        if (!(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(400, "File type not supported"));
        }

        // 创建文件保存目录（如果不存在）
        String uploadDir = "E:/111AIweb/backend/src/main/resources/static/jatest"; // 示例路径，根据实际情况修改
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs(); // 创建目录
        }

            // 生成唯一的文件名，避免文件名冲突
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = userId + "_" + new Date().getTime() + fileExtension;
        String filePath = uploadDir + "/" + newFilename;

        // 保存文件到本地
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(500, "Failed to save file"));
        }

        // 构造文件的访问 URL
        String avatarUrl = "http://localhost:8181/test/" + newFilename; // 示例 URL，根据实际情况修改

        // 更新用户头像 URL
        user.setAvatar(avatarUrl);
        usersRepository.save(user);

        return ResponseEntity.ok(new Response(200, "Avatar uploaded successfully"));
    }
//
//    // 编辑个人简介的接口
    @PutMapping("/edit-bio/{userId}")
    public Response editBio(@PathVariable Long userId, @RequestBody EditBioRequest editBioRequest) {
        String newBio = editBioRequest.getBio();

        Users user = usersRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setBio(newBio);
            usersRepository.save(user);
            return new Response(200, "Bio updated successfully");
        } else {
            return new Response(404, "User not found");
        }
    }

}