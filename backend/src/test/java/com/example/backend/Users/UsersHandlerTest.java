package com.example.backend.Users;

import com.example.backend.dto.Response;
import com.example.backend.dto.Users.LoginRequest;
import com.example.backend.dto.Users.RegisterRequest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class UsersHandlerTest {

    private static final String BASE_URL = "http://localhost:8181/users";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 测试注册功能
        testRegister(restTemplate);

        // 测试登录功能
        testLogin(restTemplate);
    }

    private static void testRegister(RestTemplate restTemplate) {
        String url = BASE_URL + "/register";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("testuser1");
        registerRequest.setPassword("password1234");
        registerRequest.setEmail("1test@example.com");
        registerRequest.setPhone("1234567890");


        HttpEntity<RegisterRequest> requestEntity = new HttpEntity<>(registerRequest);

        ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);

        System.out.println("Register Response Status Code: " + response.getStatusCode());
        System.out.println("Register Response Body: " + response.getBody());
    }

    private static void testLogin(RestTemplate restTemplate) {
        String url = BASE_URL + "/login";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setName("testuser1");
        loginRequest.setPassword("password1234");

        HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(loginRequest);

        ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);

        // 获取响应头中的 Set-Cookie
        List<String> cookies = response.getHeaders().get("Set-Cookie");

        System.out.println("Login Response Status Code: " + response.getStatusCode());
        System.out.println("Login Response Body: " + response.getBody());
        System.out.println("Login Response Cookies:");
        if (cookies != null) {
            for (String cookie : cookies) {
                System.out.println(cookie);
            }
        } else {
            System.out.println("No cookies found in response.");
        }
    }
}