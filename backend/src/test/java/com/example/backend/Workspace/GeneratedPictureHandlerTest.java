package com.example.backend.Workspace;

import com.example.backend.dto.Workspace.GeneratedPictureRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Workspace.GeneratedPicture;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class GeneratedPictureHandlerTest {

    private static final String BASE_URL = "http://localhost:8181/workspace/GeneratedPicture";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 测试创建多个生成图片
        testCreateMultipleGeneratedPictures(restTemplate);

        // 测试获取所有生成图片
        testGetAllGeneratedPictures(restTemplate);

        // 测试获取某个工作空间的所有生成图片
        testGetGeneratedPicturesByWorkspaceId(restTemplate, 1L);

        // 测试删除生成图片
        testDeleteGeneratedPicture(restTemplate, 1L, 1L); // 假设删除工作空间 ID 为 1 的生成图片 ID 为 1

        // 测试配置生成图片
        testConfigureGeneratedPicture(restTemplate, 1L, 3L); // 假设配置工作空间 ID 为 1 的生成图片 ID 为 3
    }

    private static void testCreateMultipleGeneratedPictures(RestTemplate restTemplate) {
        String url = BASE_URL + "/create";
        Long workspaceId = 1L;

        for (int i = 1; i <= 10; i++) {
            GeneratedPictureRequest generatedPictureRequest = new GeneratedPictureRequest();
            generatedPictureRequest.setWorkspaceId(workspaceId);
            generatedPictureRequest.setImageUrl("http://example.com/picture" + i + ".jpg");
            generatedPictureRequest.setGenerationConfig("config for picture " + i);

            HttpEntity<GeneratedPictureRequest> requestEntity = new HttpEntity<>(generatedPictureRequest);

            ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);

            System.out.println("Create GeneratedPicture Response Status Code: " + response.getStatusCode());
            System.out.println("Create GeneratedPicture Response Body: " + response.getBody());
        }
    }

    private static void testGetAllGeneratedPictures(RestTemplate restTemplate) {
        String url = BASE_URL + "/findAll";

        // 使用 HttpEntity.EMPTY 表示请求体为空
        HttpEntity<String> requestEntity = (HttpEntity<String>) HttpEntity.EMPTY;

        ResponseEntity<List<GeneratedPicture>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<GeneratedPicture>>() {}
        );

        System.out.println("Get All GeneratedPictures Response Status Code: " + response.getStatusCode());
        System.out.println("Get All GeneratedPictures Response Body: " + response.getBody());

        // 打印每个生成图片的详细信息
        for (GeneratedPicture generatedPicture : response.getBody()) {
            System.out.println("GeneratedPicture ID: " + generatedPicture.getId());
            System.out.println("GeneratedPicture Workspace ID: " + generatedPicture.getWorkId());
            System.out.println("GeneratedPicture URL: " + generatedPicture.getImageUrl());
            System.out.println("GeneratedPicture GenerationConfig: " + generatedPicture.getGenerationConfig());
            System.out.println("--------------------------");
        }
    }

    private static void testGetGeneratedPicturesByWorkspaceId(RestTemplate restTemplate, Long workspaceId) {
        String url = BASE_URL + "/findByWorkspaceId/" + workspaceId;

        // 使用 HttpEntity.EMPTY 表示请求体为空
        HttpEntity<String> requestEntity = (HttpEntity<String>) HttpEntity.EMPTY;

        ResponseEntity<List<GeneratedPicture>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<GeneratedPicture>>() {}
        );

        System.out.println("Get GeneratedPictures by Workspace ID Response Status Code: " + response.getStatusCode());
        System.out.println("Get GeneratedPictures by Workspace ID Response Body: " + response.getBody());

        // 打印每个生成图片的详细信息
        for (GeneratedPicture generatedPicture : response.getBody()) {
            System.out.println("GeneratedPicture ID: " + generatedPicture.getId());
            System.out.println("GeneratedPicture Workspace ID: " + generatedPicture.getWorkId());
            System.out.println("GeneratedPicture URL: " + generatedPicture.getImageUrl());
            System.out.println("GeneratedPicture GenerationConfig: " + generatedPicture.getGenerationConfig());
            System.out.println("--------------------------");
        }
    }

    private static void testDeleteGeneratedPicture(RestTemplate restTemplate, Long workspaceId, Long pictureId) {
        String url = BASE_URL + "/delete/" + pictureId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("workspaceId", workspaceId.toString()); // 设置 workspaceId 请求头

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Response> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                Response.class
        );

        System.out.println("Delete GeneratedPicture Response Status Code: " + response.getStatusCode());
        System.out.println("Delete GeneratedPicture Response Body: " + response.getBody());
    }

    private static void testConfigureGeneratedPicture(RestTemplate restTemplate, Long workspaceId, Long pictureId) {
        String url = BASE_URL + "/configure/" + pictureId;

        GeneratedPictureRequest generatedPictureRequestDTO = new GeneratedPictureRequest();
        generatedPictureRequestDTO.setWorkspaceId(workspaceId);
        generatedPictureRequestDTO.setImageUrl("http://example.com/updatedPicture.jpg");
        generatedPictureRequestDTO.setGenerationConfig("updated config for picture");

        HttpEntity<GeneratedPictureRequest> requestEntity = new HttpEntity<>(generatedPictureRequestDTO);

        ResponseEntity<Response> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Response.class
        );

        System.out.println("Configure GeneratedPicture Response Status Code: " + response.getStatusCode());
        System.out.println("Configure GeneratedPicture Response Body: " + response.getBody());
    }
}