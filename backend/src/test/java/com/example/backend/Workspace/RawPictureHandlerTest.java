package com.example.backend.Workspace;

import com.example.backend.dto.Workspace.RawPictureRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Workspace.RawPicture;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;

public class RawPictureHandlerTest {

    private static final String BASE_URL = "http://localhost:8181/workspace/RawPicture";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 测试创建多个原始图片
        //testCreateMultipleRawPictures(restTemplate);

        // 测试获取所有原始图片
        //testGetAllRawPictures(restTemplate);

        // 测试获取某个工作空间的所有原始图片
        //testGetRawPicturesByWorkspaceId(restTemplate, 1L);

        // 测试删除原始图片
        //testDeleteRawPicture(restTemplate, 1L, 1L); // 假设删除工作空间 ID 为 1 的原始图片 ID 为 1

        // 测试配置原始图片
        //testConfigureRawPicture(restTemplate, 1L, 3L); // 假设配置工作空间 ID 为 1 的原始图片 ID 为 3

        testCreateRawPicture(restTemplate);
    }

    private static void testCreateMultipleRawPictures(RestTemplate restTemplate) {
        String url = BASE_URL + "/create";
        Long workspaceId = 1L;

        for (int i = 1; i <= 10; i++) {
            RawPictureRequest rawPictureRequest = new RawPictureRequest();
            rawPictureRequest.setWorkId(workspaceId);
            rawPictureRequest.setImageUrl("http://example.com/picture" + i + ".jpg");

            HttpEntity<RawPictureRequest> requestEntity = new HttpEntity<>(rawPictureRequest);

            ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);

            System.out.println("Create RawPicture Response Status Code: " + response.getStatusCode());
            System.out.println("Create RawPicture Response Body: " + response.getBody());
        }
    }

    private static void testGetAllRawPictures(RestTemplate restTemplate) {
        String url = BASE_URL + "/findAll";

        // 使用 HttpEntity.EMPTY 表示请求体为空
        HttpEntity<String> requestEntity = (HttpEntity<String>) HttpEntity.EMPTY;

        ResponseEntity<List<RawPicture>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<RawPicture>>() {}
        );

        System.out.println("Get All RawPictures Response Status Code: " + response.getStatusCode());
        System.out.println("Get All RawPictures Response Body: " + response.getBody());

        // 打印每个原始图片的详细信息
        for (RawPicture rawPicture : response.getBody()) {
            System.out.println("RawPicture ID: " + rawPicture.getId());
            System.out.println("RawPicture Workspace ID: " + rawPicture.getWorkId());
            System.out.println("RawPicture URL: " + rawPicture.getImageUrl());
            System.out.println("--------------------------");
        }
    }

    private static void testGetRawPicturesByWorkspaceId(RestTemplate restTemplate, Long workspaceId) {
        String url = BASE_URL + "/findByWorkspaceId/" + workspaceId;

        // 使用 HttpEntity.EMPTY 表示请求体为空
        HttpEntity<String> requestEntity = (HttpEntity<String>) HttpEntity.EMPTY;

        ResponseEntity<List<RawPicture>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<RawPicture>>() {}
        );

        System.out.println("Get RawPictures by Workspace ID Response Status Code: " + response.getStatusCode());
        System.out.println("Get RawPictures by Workspace ID Response Body: " + response.getBody());

        // 打印每个原始图片的详细信息
        for (RawPicture rawPicture : response.getBody()) {
            System.out.println("RawPicture ID: " + rawPicture.getId());
            System.out.println("RawPicture Workspace ID: " + rawPicture.getWorkId());
            System.out.println("RawPicture URL: " + rawPicture.getImageUrl());
            System.out.println("--------------------------");
        }
    }

    private static void testDeleteRawPicture(RestTemplate restTemplate, Long workspaceId, Long pictureId) {
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

        System.out.println("Delete RawPicture Response Status Code: " + response.getStatusCode());
        System.out.println("Delete RawPicture Response Body: " + response.getBody());
    }

    private static void testConfigureRawPicture(RestTemplate restTemplate, Long workspaceId, Long pictureId) {
        String url = BASE_URL + "/configure/" + pictureId;

        RawPictureRequest rawPictureRequestDTO = new RawPictureRequest();
        rawPictureRequestDTO.setWorkId(workspaceId);
        rawPictureRequestDTO.setImageUrl("http://example.com/updatedPicture.jpg");

        HttpEntity<RawPictureRequest> requestEntity = new HttpEntity<>(rawPictureRequestDTO);

        ResponseEntity<Response> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Response.class
        );

        System.out.println("Configure RawPicture Response Status Code: " + response.getStatusCode());
        System.out.println("Configure RawPicture Response Body: " + response.getBody());
    }

    private static void testCreateRawPicture(RestTemplate restTemplate) {
        String url = "http://localhost:8181/workspace/RawPicture/create"; // 假设这是创建原始图片的接口地址

        // 创建文件对象
        File file = new File("example.jpg"); // 替换为实际存在的图片文件路径

        // 构建请求参数
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 创建 MultiValueMap 作为请求体
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(file));
        body.add("workspaceId", 1L); // 添加工作区 ID 参数，假设 workspaceId 为 1

        // 创建 HttpEntity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 发起 POST 请求
        ResponseEntity<Response> response = restTemplate.postForEntity(url, requestEntity, Response.class);

        // 输出响应结果
        System.out.println("Create Raw Picture Response Status Code: " + response.getStatusCode());
        System.out.println("Create Raw Picture Response Body: " + response.getBody());
    }





}