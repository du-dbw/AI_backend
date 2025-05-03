package com.example.backend.Workspace;

import com.example.backend.dto.Workspace.PublishRequest;
import com.example.backend.dto.Workspace.WorkspaceRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Workspace.Workspace;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class WorkspaceHandlerTest {

    private static final String BASE_URL = "http://localhost:8181/workspace";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 测试创建多个工作空间
        testCreateMultipleWorkspaces(restTemplate);

        // 测试获取所有工作空间
        testGetAllWorkspaces(restTemplate);

        // 测试获取某个用户的所有工作空间
        testGetWorkspacesByUserId(restTemplate, 1L);

        // 测试删除工作空间
        testDeleteWorkspace(restTemplate, 1L, 1L); // 假设删除用户 ID 为 1 的工作空间 ID 为 1

        // 测试配置工作空间
        testConfigureWorkspace(restTemplate, 1L, 3L); // 假设配置用户 ID 为 1 的工作空间 ID 为 1

        // 测试发布图片到工作空间
        testPublishImage(restTemplate, 100L, 100L); // 假设用户 ID 为 100，工作空间 ID 为 100
    }

    private static void testCreateMultipleWorkspaces(RestTemplate restTemplate) {
        String url = BASE_URL + "/create";
        String[] workspaceNames = {"Workspace1", "Workspace2", "Workspace3", "Workspace4", "Workspace5", "Workspace6", "Workspace7", "Workspace8", "Workspace9", "Workspace10"};
        Long userId = 1L;

        for (String name : workspaceNames) {
            WorkspaceRequest workspaceRequest = new WorkspaceRequest();
            workspaceRequest.setName(name);
            workspaceRequest.setDescription("Description for " + name);
            workspaceRequest.setUserId(userId);

            HttpEntity<WorkspaceRequest> requestEntity = new HttpEntity<>(workspaceRequest);

            ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);

            System.out.println("Create Workspace Response Status Code: " + response.getStatusCode());
            System.out.println("Create Workspace Response Body: " + response.getBody());
        }
    }

    private static void testGetAllWorkspaces(RestTemplate restTemplate) {
        String url = BASE_URL + "/findAll";

        // 设置请求头以包含 userId
        HttpHeaders headers = new HttpHeaders();
        headers.set("userId", "1"); // 假设 userId 是 1

        // 创建 HttpEntity 对象，包含请求头和空请求体
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // 使用 ResponseEntity 来接收响应
        ResponseEntity<List<Workspace>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Workspace>>() {}
        );

        System.out.println("Get All Workspaces Response Status Code: " + response.getStatusCode());
        System.out.println("Get All Workspaces Response Body: " + response.getBody());

        // 打印每个工作空间的详细信息
        for (Workspace workspace : response.getBody()) {
            System.out.println("Workspace ID: " + workspace.getId());
            System.out.println("Workspace Name: " + workspace.getName());
            System.out.println("Workspace Description: " + workspace.getDescription());
            System.out.println("Workspace User ID: " + workspace.getUserId());
            System.out.println("--------------------------");
        }
    }

    private static void testGetWorkspacesByUserId(RestTemplate restTemplate, Long userId) {
        String url = BASE_URL + "/findByUserId";

        HttpHeaders headers = new HttpHeaders();
        headers.set("userId", userId.toString()); // 设置 userId 请求头

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Workspace>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<Workspace>>() {}
        );

        System.out.println("Get Workspaces by User ID Response Status Code: " + response.getStatusCode());
        System.out.println("Get Workspaces by User ID Response Body: " + response.getBody());

        // 打印每个工作空间的详细信息
        for (Workspace workspace : response.getBody()) {
            System.out.println("Workspace ID: " + workspace.getId());
            System.out.println("Workspace Name: " + workspace.getName());
            System.out.println("Workspace Description: " + workspace.getDescription());
            System.out.println("Workspace User ID: " + workspace.getUserId());
            System.out.println("--------------------------");
        }
    }


    private static void testDeleteWorkspace(RestTemplate restTemplate, Long userId, Long workspaceId) {
        String url = BASE_URL + "/delete/" + workspaceId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("userId", userId.toString()); // 设置 userId 请求头

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Response> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                requestEntity,
                Response.class
        );

        System.out.println("Delete Workspace Response Status Code: " + response.getStatusCode());
        System.out.println("Delete Workspace Response Body: " + response.getBody());
    }

    private static void testConfigureWorkspace(RestTemplate restTemplate, Long userId, Long workspaceId) {
        String url = BASE_URL + "/configure/" + workspaceId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("userId", userId.toString()); // 设置 userId 请求头

        WorkspaceRequest workspaceRequestDTO = new WorkspaceRequest();
        workspaceRequestDTO.setName("Updated Workspace Name");
        workspaceRequestDTO.setDescription("Updated description for the workspace");
        workspaceRequestDTO.setUserId(userId);

        HttpEntity<WorkspaceRequest> requestEntity = new HttpEntity<>(workspaceRequestDTO, headers);

        ResponseEntity<Response> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Response.class
        );

        System.out.println("Configure Workspace Response Status Code: " + response.getStatusCode());
        System.out.println("Configure Workspace Response Body: " + response.getBody());
    }

    private static void testPublishImage(RestTemplate restTemplate, Long userId, Long workspaceId) {
        String url = BASE_URL + "/publish";

        // 设置请求头以包含 userId
        HttpHeaders headers = new HttpHeaders();
        headers.set("userId", userId.toString());

        // 创建 PublishRequest 对象
        PublishRequest publishRequest = new PublishRequest();
        publishRequest.setImageUrl("https://example.com/path/to/generated/image.jpg");
        publishRequest.setWorkspaceId(workspaceId);

        // 创建 HttpEntity 对象，包含请求头和请求体
        HttpEntity<PublishRequest> requestEntity = new HttpEntity<>(publishRequest, headers);

        // 使用 ResponseEntity 来接收响应
        ResponseEntity<Response> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Response.class
        );

        System.out.println("Publish Image Response Status Code: " + response.getStatusCode());
        System.out.println("Publish Image Response Body: " + response.getBody());
    }


}