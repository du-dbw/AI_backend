package com.example.backend;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class test111 {

    private static final String BASE_URL = "http://hz-jcy-1.matpool.com:26570/";

    public static void main(String[] args) {
        // 创建 RestTemplate 对象
        RestTemplate restTemplate = new RestTemplate();

        // 构建 POST 请求
        String url = BASE_URL;

        // 示例请求体
        String requestBody = "{\"key\":\"value\"}";

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 创建 HttpEntity
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 执行 POST 请求并获取响应
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<String>() {} // 相应类型
        );

        // 打印响应状态码和内容
        System.out.println("Response status code: " + response.getStatusCode());
        System.out.println("Response content: " + response.getBody());
    }
}