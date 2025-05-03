package com.example.backend.Follow;

import com.example.backend.dto.Follow.FollowRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Follow.Follow;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class FollowHandlerTest {

    private static final String BASE_URL = "http://localhost:8181/follow";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 测试关注功能
        testFollow(restTemplate,10);

        // 测试取消关注功能
        testUnfollow(restTemplate);

        // 测试获取关注列表功能
        testGetFollowing(restTemplate);

        // 测试获取粉丝列表功能
        testGetFollowers(restTemplate);
    }

    private static void testFollow(RestTemplate restTemplate, int count) {
        String url = BASE_URL + "/follow";
        Long followerId = 1L; // 关注者的 ID

        for (int i = 1; i <= count; i++) {
            FollowRequest followRequest = new FollowRequest();
            followRequest.setFollowerId(followerId);
            followRequest.setFolloweeId((long) (2 + i)); // 被关注者的 ID，从3开始递增

            HttpEntity<FollowRequest> requestEntity = new HttpEntity<>(followRequest);

            ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);

            System.out.printf("Follow Response for followeeId %d - Status Code: %s, Body: %s%n",
                    followRequest.getFolloweeId(),
                    response.getStatusCode(),
                    response.getBody());
        }
    }

    private static void testUnfollow(RestTemplate restTemplate) {


        String url = BASE_URL + "/unfollow";

        FollowRequest followRequest = new FollowRequest();


        followRequest.setFollowerId(1L); // 关注者的 ID
        followRequest.setFolloweeId(2L); // 被关注者的 ID

        HttpEntity<FollowRequest> requestEntity = new HttpEntity<>(followRequest);
        ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);
        System.out.println("Unfollow Response Status Code: " + response.getStatusCode());
        System.out.println("Unfollow Response Body: " + response.getBody());
    }

    private static void testGetFollowing(RestTemplate restTemplate) {
        String url = BASE_URL + "/following/{userId}";

        Long userId = 1L; // 用户的 ID

        // 将返回值类型改为 List<Follow>
        ResponseEntity<List<Follow>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Follow>>() {}, userId);

        System.out.println("Get Following Response Status Code: " + response.getStatusCode());
        System.out.println("Get Following Response Body: " + response.getBody());
    }

    private static void testGetFollowers(RestTemplate restTemplate) {
        String url = BASE_URL + "/followers/{userId}";

        Long userId = 1L; // 用户的 ID

        // 将返回值类型改为 List<Follow>
        ResponseEntity<List<Follow>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Follow>>() {}, userId);

        System.out.println("Get Followers Response Status Code: " + response.getStatusCode());
        System.out.println("Get Followers Response Body: " + response.getBody());
    }
}