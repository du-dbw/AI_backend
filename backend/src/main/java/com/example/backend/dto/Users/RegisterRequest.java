package com.example.backend.dto.Users;
import jakarta.validation.constraints.*;

public class RegisterRequest {
    //@NotBlank(message = "用户名不能为空")
    @Size(min = 1, max = 128, message = "用户名长度必须在1到128个字符之间")
    private String name;

    //@NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 128, message = "密码长度必须在6到128个字符之间")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 128, message = "邮箱长度不能超过128个字符")
    private String email;

    @NotBlank(message = "手机号不能为空")
    @Size(max = 128, message = "手机号长度不能超过128个字符")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确") // 中国手机号正则
    private String phone;
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
}