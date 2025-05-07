package com.example.backend.Users;

import com.example.backend.dto.Response;
import com.example.backend.dto.Users.EditBioRequest;
import com.example.backend.dto.Users.LoginRequest;
import com.example.backend.dto.Users.RegisterRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UsersHandlerTest {

    private static final String BASE_URL = "http://localhost:8181/users";
    private static final String BASE_URL_UPLOAD_AVATAR = "http://localhost:8181/users/upload-avatar";
    private static final String BASE_URL_EDIT_BIO = "http://localhost:8181/edit-bio";

    private static RestTemplate restTemplate = new RestTemplate();
    private static List<String> cookies;

    public static void main(String[] args) {
        // 测试注册功能
        //testRegister(restTemplate);

        // 测试登录功能
        testLogin(restTemplate);

        // 测试上传头像功能
        testUploadAvatar(restTemplate);

        // 测试编辑个人简介功能
        //testEditBio(restTemplate);
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
        cookies = response.getHeaders().get("Set-Cookie");

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

    private static void testUploadAvatar(RestTemplate restTemplate) {
        String url = BASE_URL_UPLOAD_AVATAR + "/{userId}";

        // 创建文件对象（这里使用示例文件路径，你需要替换为实际存在的文件路径）
        File file = new File("example.jpg"); // 替换为实际存在的图片文件路径

        // 构建请求参数
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 添加 cookie 到请求头
//        if (cookies != null) {
//            // 遍历登录返回的 cookies
//            for (String cookie : cookies) {
//                // 分割 cookie 字符串，提取 cookie 名称和值
//                String[] cookieParts = cookie.split(";");
//                if (cookieParts.length > 0) {
//                    headers.add("Cookie", cookieParts[0]); // 只添加 cookie 名称和值部分
//                }
//            }
//        }

        // 创建 MultiValueMap 作为请求体
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(file));

        // 创建 HttpEntity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 指定 userId
        Map<String, Long> uriVariables = Collections.singletonMap("userId", 1L); // 替换为实际存在的 userId

        // 发起 POST 请求
        ResponseEntity<Response> response = restTemplate.postForEntity(url, requestEntity, Response.class, uriVariables);

        // 输出响应结果
        System.out.println("Upload Avatar Response Status Code: " + response.getStatusCode());
        System.out.println("Upload Avatar Response Body: " + response.getBody());
    }

    private static void testEditBio(RestTemplate restTemplate) {
        String url = BASE_URL_EDIT_BIO + "/{userId}";

        // 构建请求体
        EditBioRequest editBioRequest = new EditBioRequest();
        editBioRequest.setBio("Updated bio text");

        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // 设置请求内容类型为 JSON

        // 添加 cookie 到请求头
        if (cookies != null) {
            // 遍历登录返回的 cookies
            for (String cookie : cookies) {
                // 分割 cookie 字符串，提取 cookie 名称和值
                String[] cookieParts = cookie.split(";");
                if (cookieParts.length > 0) {
                    headers.add("Cookie", cookieParts[0]); // 只添加 cookie 名称和值部分
                }
            }
        }

        HttpEntity<EditBioRequest> requestEntity = new HttpEntity<>(editBioRequest, headers);

        // 指定 userId
        Map<String, Long> uriVariables = Collections.singletonMap("userId", 1L); // 替换为实际存在的 userId

        // 发起 PUT 请求
        ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Response.class, uriVariables);

        // 输出响应结果
        System.out.println("Edit Bio Response Status Code: " + response.getStatusCode());
        System.out.println("Edit Bio Response Body: " + response.getBody());
    }
}