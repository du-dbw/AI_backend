package com.example.backend.Works.Comments;

import com.example.backend.dto.Works.Comments.Comments_LikesRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Works.Comments.Comments_Likes;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class Comments_LikesHandlerTest {

    private static final String BASE_URL = "http://localhost:8181/comments-likes";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 测试添加点赞功能
        testAddCommentLikes(restTemplate, 3);

        // 测试删除点赞功能
        testDeleteCommentLike(restTemplate);

        // 测试获取某个评论的所有点赞
        testGetLikesByComment(restTemplate);

        // 测试获取某个用户的所有点赞
        testGetLikesByUser(restTemplate);
    }

    private static void testAddCommentLikes(RestTemplate restTemplate, int count) {
        String url = BASE_URL + "/add";
        Long commentId = 1L; // 评论的 ID

        for (int i = 1; i <= count; i++) {
            Comments_LikesRequest likesRequest = new Comments_LikesRequest();
            likesRequest.setCommentId(commentId);
            likesRequest.setUserId((long) (1 + i)); // 用户的 ID，从2开始递增

            HttpEntity<Comments_LikesRequest> requestEntity = new HttpEntity<>(likesRequest);

            ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);

            System.out.printf("Add Like Response for commentID %d and userID %d - Status Code: %s, Body: %s%n",
                    likesRequest.getCommentId(),
                    likesRequest.getUserId(),
                    response.getStatusCode(),
                    response.getBody());
        }
    }

    private static void testDeleteCommentLike(RestTemplate restTemplate) {
        String url = BASE_URL + "/delete";

        Comments_LikesRequest likesRequest = new Comments_LikesRequest();
        likesRequest.setCommentId(1L); // 评论的 ID
        likesRequest.setUserId(2L); // 用户的 ID

        HttpEntity<Comments_LikesRequest> requestEntity = new HttpEntity<>(likesRequest);
        ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);
        System.out.println("Delete Like Response Status Code: " + response.getStatusCode());
        System.out.println("Delete Like Response Body: " + response.getBody());
    }

    private static void testGetLikesByComment(RestTemplate restTemplate) {
        String url = BASE_URL + "/bycomment/{commentId}";

        Long commentId = 1L; // 评论的 ID

        ResponseEntity<List<Comments_Likes>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Comments_Likes>>() {}, commentId);

        System.out.println("Get Likes By Comment Response Status Code: " + response.getStatusCode());
        System.out.println("Get Likes By Comment Response Body: " + response.getBody());
    }

    private static void testGetLikesByUser(RestTemplate restTemplate) {
        String url = BASE_URL + "/byuser/{userId}";

        Long userId = 3L; // 用户的 ID

        ResponseEntity<List<Comments_Likes>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Comments_Likes>>() {}, userId);

        System.out.println("Get Likes By User Response Status Code: " + response.getStatusCode());
        System.out.println("Get Likes By User Response Body: " + response.getBody());
    }
}