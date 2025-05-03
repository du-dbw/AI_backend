package com.example.backend.Favorites;

import com.example.backend.controller.FavoritesHandler;
import com.example.backend.dto.Favorites.FavoritesRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Favorites.Favorites;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class FavoritesHandlerTest {

    private static final String BASE_URL = "http://localhost:8181/favorites";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 测试添加多个收藏
        testAddMultipleFavorites(restTemplate);

        // 测试获取所有收藏记录
        testGetAllFavorites(restTemplate);

        // 测试获取某个用户的所有收藏记录
        testGetFavoritesByUserId(restTemplate, 1L);

        // 测试删除收藏记录
        testRemoveFavorite(restTemplate, 1L, 1L); // 假设删除用户 ID 为 1 的作品 ID 为 1 的收藏记录
    }

    private static void testAddMultipleFavorites(RestTemplate restTemplate) {
        String url = BASE_URL + "/add";
        Long userId = 1L;
        Long[] workIds = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L};

        for (Long workId : workIds) {
            FavoritesRequest favoritesRequest = new FavoritesRequest();
            favoritesRequest.setUserID(userId);
            favoritesRequest.setWorkID(workId);

            HttpEntity<FavoritesRequest> requestEntity = new HttpEntity<>(favoritesRequest);

            ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);

            System.out.println("Add Favorite Response Status Code: " + response.getStatusCode());
            System.out.println("Add Favorite Response Body: " + response.getBody());
        }
    }

    private static void testGetAllFavorites(RestTemplate restTemplate) {
        String url = BASE_URL + "/findAll";

        ResponseEntity<List<Favorites>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Favorites>>() {}
        );

        System.out.println("Get All Favorites Response Status Code: " + response.getStatusCode());
        System.out.println("Get All Favorites Response Body: " + response.getBody());


    }

    private static void testGetFavoritesByUserId(RestTemplate restTemplate, Long userId) {
        String url = BASE_URL + "/findByUserId";

        HttpHeaders headers = new HttpHeaders();
        headers.set("userId", userId.toString());

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Favorites>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Favorites>>() {}
        );

        System.out.println("Get Favorites by User ID Response Status Code: " + response.getStatusCode());
        System.out.println("Get Favorites by User ID Response Body: " + response.getBody());

    }


    private static void testRemoveFavorite(RestTemplate restTemplate, Long userId, Long workId) {
        String url = BASE_URL + "/remove";

        FavoritesRequest favoritesRequest = new FavoritesRequest();
        favoritesRequest.setUserID(userId);
        favoritesRequest.setWorkID(workId);

        HttpEntity<FavoritesRequest> requestEntity = new HttpEntity<>(favoritesRequest);

        try {
            ResponseEntity<Response> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Response.class
            );
            System.out.println("Remove Favorite Response Status Code: " + response.getStatusCode());
            System.out.println("Remove Favorite Response Body: " + response.getBody());
        } catch (Exception e) {
            System.out.println("Remove Favorite Error: " + e.getMessage());
        }
    }
}