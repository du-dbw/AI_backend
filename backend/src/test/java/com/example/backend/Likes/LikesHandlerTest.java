package com.example.backend.Likes;

import com.example.backend.dto.Likes.LikesRequest;
import com.example.backend.dto.Response;
import org.springframework.core.ParameterizedTypeReference;
import com.example.backend.entity.Likes.Likes;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class LikesHandlerTest {

    private static final String BASE_URL = "http://localhost:8181/likes";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 测试添加点赞功能
        testAddLikes(restTemplate, 5);

        // 测试删除点赞功能
        testDeleteLike(restTemplate);

        // 测试获取某个作品的所有点赞记录
        testGetLikesByWork(restTemplate);

        // 测试获取某个用户的所有点赞记录
        testGetLikesByUser(restTemplate);
    }

    private static void testAddLikes(RestTemplate restTemplate, int count) {
        String url = BASE_URL + "/add";
        Long workID = 2L; // 作品的 ID

        for (int i = 1; i <= count; i++) {
            LikesRequest likesRequest = new LikesRequest();
            likesRequest.setWorkId(workID);
            likesRequest.setUserId((long) (1 + i)); // 点赞用户的 ID，从2开始递增

            HttpEntity<LikesRequest> requestEntity = new HttpEntity<>(likesRequest);

            ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);

            System.out.printf("Add Like Response for workID %d and userID %d - Status Code: %s, Body: %s%n",
                    likesRequest.getWorkId(),
                    likesRequest.getUserId(),
                    response.getStatusCode(),
                    response.getBody());
        }
    }

    private static void testDeleteLike(RestTemplate restTemplate) {
        String url = BASE_URL + "/delete";

        LikesRequest likesRequest = new LikesRequest();
        likesRequest.setWorkId(2L); // 作品的 ID
        likesRequest.setUserId(5L); // 点赞用户的 ID

        HttpEntity<LikesRequest> requestEntity = new HttpEntity<>(likesRequest);
        ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);
        System.out.println("Delete Like Response Status Code: " + response.getStatusCode());
        System.out.println("Delete Like Response Body: " + response.getBody());
    }

    private static void testGetLikesByWork(RestTemplate restTemplate) {
        String url = BASE_URL + "/bywork/{workId}";

        Long workId = 1L; // 作品的 ID

        // 将返回值类型改为 List<Likes>
        ResponseEntity<List<Likes>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Likes>>() {}, workId);

        System.out.println("Get Likes By Work Response Status Code: " + response.getStatusCode());
        System.out.println("Get Likes By Work Response Body: " + response.getBody());
    }

    private static void testGetLikesByUser(RestTemplate restTemplate) {
        String url = BASE_URL + "/byuser/{userId}";

        Long userId = 3L; // 用户的 ID

        // 将返回值类型改为 List<Likes>
        ResponseEntity<List<Likes>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Likes>>() {}, userId);

        System.out.println("Get Likes By User Response Status Code: " + response.getStatusCode());
        System.out.println("Get Likes By User Response Body: " + response.getBody());
    }
}