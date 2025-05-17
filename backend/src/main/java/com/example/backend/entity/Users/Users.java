package com.example.backend.entity.Users;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
//@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 128, nullable = false)
    private String name;

    @Column(length = 128, nullable = false)
    private String password;

    @Column(length = 128, nullable = false, unique = true)
    private String email;

    @Column(length = 128, nullable = false)
    private String phone;

    // 新增用户头像属性并设置默认值
    @Column(length = 256)
    private String avatar = "default-avatar.jpg"; // 默认值

    // 新增个人简介属性并设置默认值
    @Column(length = 512)
    private String bio = "No bio available"; // 默认值

    @Column(length = 64)
    private String token; // 存储生成的Token

    @Column
    private LocalDateTime tokenExpiry;

    // 默认构造方法
    public Users() {
    }

    // 全参构造方法
    public Users(String name, String password, String email, String phone, String avatar, String bio) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.bio = bio;

    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // 新增用户头像的 Getter 和 Setter 方法
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    // 新增个人简介的 Getter 和 Setter 方法
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(LocalDateTime tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", bio='" + bio + '\'' +
                "}\n";
    }

}