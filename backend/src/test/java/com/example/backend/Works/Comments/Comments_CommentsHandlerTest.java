package com.example.backend.Works.Comments;

import com.example.backend.dto.Works.Comments.Comments_CommentsRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Works.Comments.Comments_Comments;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class Comments_CommentsHandlerTest {

    private static final String BASE_URL = "http://localhost:8181/comments-comments";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 测试添加子评论功能
        testAddCommentComments(restTemplate, 4);

        // 测试删除子评论功能
        testDeleteCommentComment(restTemplate);

        // 测试获取某个父评论的所有子评论
        testGetCommentCommentsByParent(restTemplate);

        // 测试获取某个用户的所有子评论
        testGetCommentCommentsByUser(restTemplate);
    }

    private static void testAddCommentComments(RestTemplate restTemplate, int count) {
        String url = BASE_URL + "/add";
        Long parentId = 1L; // 父评论的 ID

        for (int i = 1; i <= count; i++) {
            Comments_CommentsRequest commentsRequest = new Comments_CommentsRequest();
            commentsRequest.setParentId(parentId);
            commentsRequest.setUserID((long) (1 + i)); // 用户的 ID，从2开始递增
            commentsRequest.setContent("This is a test sub-comment for parent " + parentId + " by user " + commentsRequest.getUserID());

            HttpEntity<Comments_CommentsRequest> requestEntity = new HttpEntity<>(commentsRequest);

            ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);

            System.out.printf("Add Sub-Comment Response for parentId %d and userID %d - Status Code: %s, Body: %s%n",
                    commentsRequest.getParentId(),
                    commentsRequest.getUserID(),
                    response.getStatusCode(),
                    response.getBody());
        }
    }

    private static void testDeleteCommentComment(RestTemplate restTemplate) {
        String url = BASE_URL + "/delete";

        Comments_CommentsRequest commentsRequest = new Comments_CommentsRequest();
        commentsRequest.setParentId(1L); // 父评论的 ID
        commentsRequest.setUserID(2L); // 用户的 ID

        HttpEntity<Comments_CommentsRequest> requestEntity = new HttpEntity<>(commentsRequest);
        ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);
        System.out.println("Delete Sub-Comment Response Status Code: " + response.getStatusCode());
        System.out.println("Delete Sub-Comment Response Body: " + response.getBody());
    }

    private static void testGetCommentCommentsByParent(RestTemplate restTemplate) {
        String url = BASE_URL + "/byparent/{parentId}";

        Long parentId = 1L; // 父评论的 ID

        ResponseEntity<List<Comments_Comments>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Comments_Comments>>() {}, parentId);

        System.out.println("Get Sub-Comments By Parent Response Status Code: " + response.getStatusCode());
        System.out.println("Get Sub-Comments By Parent Response Body: " + response.getBody());
    }

    private static void testGetCommentCommentsByUser(RestTemplate restTemplate) {
        String url = BASE_URL + "/byuser/{userId}";

        Long userId = 3L; // 用户的 ID

        ResponseEntity<List<Comments_Comments>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Comments_Comments>>() {}, userId);

        System.out.println("Get Sub-Comments By User Response Status Code: " + response.getStatusCode());
        System.out.println("Get Sub-Comments By User Response Body: " + response.getBody());
    }
}