package com.example.backend.controller;

import com.example.backend.dto.Users.EditBioRequest;
import com.example.backend.dto.Users.LoginRequest;
import com.example.backend.dto.Users.RegisterRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Favorites.Favorites;
import com.example.backend.entity.Users.Users;
import com.example.backend.repository.Favorites.FavoritesRepository;
import com.example.backend.repository.Users.UsersRepository;
import com.example.backend.repository.Works.WorksRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.Cookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


import com.example.backend.entity.Works.Works;
import org.springframework.web.server.ResponseStatusException;


import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Optional;
import java.util.stream.Collectors;
import com.example.backend.dto.Users.UserPreviewData;
import com.example.backend.repository.Users.Follow.FollowRepository;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UsersHandler {
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private FavoritesRepository favoritesRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private WorksRepository worksRepository;

    @GetMapping("/findAll")
    public List<Users> findAll() {
        return usersRepository.findAll();
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String name = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Users user = usersRepository.findByName(name);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            //System.out.println("User found and password matches");
            // 生成唯一Token并设置过期时间
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setTokenExpiry(LocalDateTime.now().plusHours(1)); // 1小时后过期
            usersRepository.save(user);
            //System.out.println("Token generated and saved: " + token);
            // 创建安全Cookie
            Cookie cookie = new Cookie("userToken", token);
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            cookie.setHttpOnly(true); // 防止XSS
            cookie.setSecure(true); // 仅HTTPS传输
            response.addCookie(cookie);


            return new Response(0, "Login successful");
        } else {
            return new Response(1, "Invalid username or password");
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

        String name = registerRequest.getUsername();
        System.out.println("register");
        System.out.println("name = " + name);
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

        return new Response(0, "Registration successful");
    }


    @PostMapping("/avatar/upload")
    public ResponseEntity<Response<Map<String, Object>>> uploadAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        // 提取 Token 并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response<>(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", null));
        }

        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response<>(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", null));
        }

        // 检查文件是否为空
        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(HttpStatus.BAD_REQUEST.value(), "File is empty", null));
        }

        // 检查文件类型是否为图片
        String contentType = file.getContentType();
        if (!(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(HttpStatus.BAD_REQUEST.value(), "File type not supported", null));
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
        String newFilename = new Date().getTime() + fileExtension;
        String filePath = uploadDir + "/" + newFilename;

        // 保存文件到本地
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save file", null));
        }

        // 构造文件的访问 URL
        String avatarUrl = "http://localhost:8181/test/" + newFilename; // 示例 URL，根据实际情况修改

        // 这里先不与用户绑定，等之后再绑定
        /*
        // 更新用户头像 URL
        currentUser.setAvatar(avatarUrl);
        usersRepository.save(currentUser);
        */

        // 返回上传成功的响应，包含头像 URL
        Map<String, Object> data = new HashMap<>();
        data.put("avatar_url", avatarUrl);
        //return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), "Success", data));
        return ResponseEntity.ok(new Response<>(0, "Success", data));
    }


    // 获取作者信息
    @GetMapping("/{username}/preview")
    public Response preview(@PathVariable String username , HttpServletRequest request) {
        Users user = usersRepository.findByName(username);
        if (user != null) {

            Long followersCount = followRepository.countByFolloweeId(user.getId());
            boolean followed = false;

            String token = extractToken(request);
            if (token != null) {
                Users currentUser = usersRepository.findByToken(token);
                if (currentUser != null && currentUser.getTokenExpiry().isAfter(LocalDateTime.now())) {
                    // 检查当前用户是否已关注该作者
                    followed = followRepository.existsByFollowerIdAndFolloweeId(currentUser.getId(), user.getId());
                }
            }

            return new Response(0, "获取成功", new UserPreviewData(followersCount, user.getAvatar(), followed));
        } else {
            return new Response(404, "用户未找到");
        }
    }

    // 获取用户个人资料
    @GetMapping("/{username}/profile")
    public Response getProfile(@PathVariable String username, HttpServletRequest request) {
        // 从请求中提取Token并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return new Response(401, "Unauthorized");
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return new Response(401, "Unauthorized");
        }

        if (!currentUser.getName().equals(username)) {
            return new Response(403, "Forbidden");
        }

        // 查询目标用户信息
        Users targetUser = usersRepository.findByName(username);
        if (targetUser == null) {
            return new Response(404, "User not found");
        }


        // 统计目标用户的粉丝数、关注数、作品数和收藏数
        Long followersCount = followRepository.countByFolloweeId(targetUser.getId());
        Long followingsCount = followRepository.countByFollowerId(targetUser.getId());
        Long worksCount = worksRepository.countByUserId(targetUser.getId());
        Long collectionsCount = favoritesRepository.countByUserID(targetUser.getId());

        // 构建响应数据
        Map<String, Object> data = new HashMap<>();
        data.put("username", targetUser.getName());
        data.put("email", targetUser.getEmail());
        data.put("bio", targetUser.getBio());
        data.put("avatar_url", targetUser.getAvatar());
        data.put("followers_count", followersCount);
        data.put("followings_count", followingsCount);
        data.put("works_count", worksCount);
        data.put("collections_count", collectionsCount);

        return new Response(0, "Profile retrieved successfully", data);
    }

    // 更新用户个人资料
    @PutMapping("/{username}/profile")
    public Response updateProfile(@PathVariable String username, @RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        // 从请求中提取Token并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return new Response(401, "Unauthorized");
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return new Response(401, "Unauthorized");
        }

        // 验证请求的用户名是否与当前登录用户一致
        if (!currentUser.getName().equals(username)) {
            return new Response(403, "Forbidden");
        }

        // 提取新的邮箱地址（如果有）
        String newEmail = requestBody.get("email");
        // 邮箱唯一性验证（如果提供了新的邮箱）
        if (newEmail != null && !newEmail.isEmpty() && !currentUser.getEmail().equals(newEmail)) {
            // 检查数据库中是否存在相同的邮箱
            boolean emailExists = usersRepository.existsByEmail(newEmail);
            if (emailExists) {
                return new Response(400, "Email already exists");
            }
            // 如果没有冲突，更新邮箱
            currentUser.setEmail(newEmail);
        }

        // 更新用户信息
        currentUser.setEmail(requestBody.getOrDefault("email", currentUser.getEmail()));
        currentUser.setBio(requestBody.getOrDefault("bio", currentUser.getBio()));
        currentUser.setAvatar(requestBody.getOrDefault("avatar_url", currentUser.getAvatar()));

        usersRepository.save(currentUser);

        return new Response(0, "Profile updated successfully");
    }

    @GetMapping("/{username}/works")
    public Response getUserWorks(
            @PathVariable String username,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpServletRequest request
    ) {
        // 从请求中提取Token并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return new Response(401, "Unauthorized");
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return new Response(401, "Unauthorized");
        }

        // 根据用户名查找用户
        Users user = usersRepository.findByName(username);
        if (user == null) {
            return new Response(404, "User not found");
        }

        // 检查当前用户与请求的用户是否一致
        if (!currentUser.getName().equals(username)) {
            return new Response(403, "You do not have permission to access this user's works");
        }

        // 构造分页请求
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        // 获取用户的所有已发布作品
        List<Works> worksList = worksRepository.findByUserIdAndPublishedTrue(user.getId(), pageable);

        // 返回成功响应
        return new Response(0, "Works retrieved successfully", worksList);
    }

    // 获取用户收藏的作品列表
    @GetMapping("/{username}/collections")
    public Response getUserCollections(
            @PathVariable String username,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpServletRequest request) {

        // 从请求中提取Token并验证用户身份
        String token = extractToken(request);
        if (token == null) {
            return new Response(401, "Unauthorized");
        }
        Users currentUser = usersRepository.findByToken(token);
        if (currentUser == null || currentUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return new Response(401, "Unauthorized");
        }

        // 根据用户名查找用户
        Users user = usersRepository.findByName(username);
        if (user == null) {
            return new Response(404, "User not found");
        }

        // 检查当前用户与请求的用户是否一致
        if (!currentUser.getName().equals(username)) {
            return new Response(403, "You do not have permission to access this user's works");
        }

        // 构造分页请求
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        // 获取用户的所有收藏记录
        List<Favorites> favoritesList = favoritesRepository.findAllByUserID(user.getId(), pageable);

        // 获取所有收藏作品的ID
        List<Long> workIds = favoritesList.stream()
                .map(favorite -> favorite.getWorkID())
                .collect(Collectors.toList());

        // 根据作品ID获取作品信息
        List<Works> worksList;
        if (!workIds.isEmpty()) {
            worksList = worksRepository.findByWorkIdIn(workIds);
        } else {
            worksList = Collections.emptyList();
        }

        // 返回成功响应
        return new Response(0, "Collections retrieved successfully", worksList);
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
