package com.example.backend.entity.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Data;

@Entity
@Data
//@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
    public long getId() {
        return id;
    }

    public void setId(long id) {
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