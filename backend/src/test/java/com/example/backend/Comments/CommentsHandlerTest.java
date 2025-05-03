package com.example.backend.Comments;

import com.example.backend.dto.Comments.CommentsRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Comments.Comments;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class CommentsHandlerTest {

    private static final String BASE_URL = "http://localhost:8181/comments";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 测试添加评论功能
        testAddComments(restTemplate, 5);

        // 测试删除评论功能
        testDeleteComment(restTemplate);

        // 测试获取某个作品的所有评论
        testGetCommentsByWork(restTemplate);

        // 测试获取某个用户的所有评论
        testGetCommentsByUser(restTemplate);
    }

    private static void testAddComments(RestTemplate restTemplate, int count) {
        String url = BASE_URL + "/add";
        Long workID = 1L; // 作品的 ID

        for (int i = 1; i <= count; i++) {
            CommentsRequest commentsRequest = new CommentsRequest();
            commentsRequest.setWorkID(workID);
            commentsRequest.setUserID((long) (1 + i)); // 评论用户的 ID，从2开始递增
            commentsRequest.setContent("This is a test comment for work " + workID + " by user " + commentsRequest.getUserID());

            HttpEntity<CommentsRequest> requestEntity = new HttpEntity<>(commentsRequest);

            ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);

            System.out.printf("Add Comment Response for workID %d and userID %d - Status Code: %s, Body: %s%n",
                    commentsRequest.getWorkID(),
                    commentsRequest.getUserID(),
                    response.getStatusCode(),
                    response.getBody());
        }
    }

    private static void testDeleteComment(RestTemplate restTemplate) {
        String url = BASE_URL + "/delete";

        CommentsRequest commentsRequest = new CommentsRequest();
        commentsRequest.setWorkID(1L); // 作品的 ID
        commentsRequest.setUserID(2L); // 评论用户的 ID

        HttpEntity<CommentsRequest> requestEntity = new HttpEntity<>(commentsRequest);
        ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);
        System.out.println("Delete Comment Response Status Code: " + response.getStatusCode());
        System.out.println("Delete Comment Response Body: " + response.getBody());
    }

    private static void testGetCommentsByWork(RestTemplate restTemplate) {
        String url = BASE_URL + "/bywork/{workId}";

        Long workId = 1L; // 作品的 ID

        // 将返回值类型改为 List<Comments>
        ResponseEntity<List<Comments>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Comments>>() {}, workId);

        System.out.println("Get Comments By Work Response Status Code: " + response.getStatusCode());
        System.out.println("Get Comments By Work Response Body: " + response.getBody());
    }

    private static void testGetCommentsByUser(RestTemplate restTemplate) {
        String url = BASE_URL + "/byuser/{userId}";

        Long userId = 3L; // 用户的 ID

        // 将返回值类型改为 List<Comments>
        ResponseEntity<List<Comments>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Comments>>() {}, userId);

        System.out.println("Get Comments By User Response Status Code: " + response.getStatusCode());
        System.out.println("Get Comments By User Response Body: " + response.getBody());
    }
}