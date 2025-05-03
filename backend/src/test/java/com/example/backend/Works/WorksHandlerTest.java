package com.example.backend.Works;

import com.example.backend.dto.Works.WorksRequest;
import com.example.backend.dto.Response;
import com.example.backend.entity.Works.Works;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WorksHandlerTest {

    private static final String BASE_URL = "http://localhost:8181/works";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 测试创建作品
        testCreateWork(restTemplate);

        // 测试更新作品
        testUpdateWork(restTemplate);

        // 测试删除作品
        testDeleteWork(restTemplate);

        // 测试获取所有作品
        testGetAllWorks(restTemplate);

        // 测试按关注列表获取作品
        testGetByFollowing(restTemplate);

        // 测试获取最新发布的作品
        testGetLatestWorks(restTemplate);

        // 测试获取热度最高的作品
        testGetPopularWorks(restTemplate);

        // 测试综合排序获取作品
        testGetComprehensiveWorks(restTemplate);
    }

    private static void testCreateWork(RestTemplate restTemplate) {
        String url = BASE_URL + "/create";

        for (int i = 0; i < 10; i++) {
            WorksRequest worksRequest = new WorksRequest();
            worksRequest.setUserId(1L); // 使用相同的用户ID或根据需要更改
            worksRequest.setTitle("Test Work Title " + (i + 1));
            worksRequest.setDescription("Test Work Description " + (i + 1));
            worksRequest.setImageUrl("https://example.com/image" + (i + 1) + ".jpg"); // 使用不同的图片URL

            // 设置不同的点赞数和评论数
            worksRequest.setLikes((long) (i + 1) * 10); // 例如：10, 20, 30, ...
            worksRequest.setComments((long) (i + 1) * 5); // 例如：5, 10, 15, ...

            // 设置不同的创建时间（如果需要）
            LocalDateTime createdTime = LocalDateTime.now().minusDays(10 - i);
            worksRequest.setCreatedTime(createdTime);

            HttpEntity<WorksRequest> requestEntity = new HttpEntity<>(worksRequest);

            ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Response.class);

            System.out.println("Create Work Response for Work " + (i + 1) + " - Status Code: " + response.getStatusCode());
            System.out.println("Create Work Response Body: " + response.getBody());
        }
    }

    private static void testUpdateWork(RestTemplate restTemplate) {
        String url = BASE_URL + "/update/2"; // 假设作品ID为1

        WorksRequest worksRequest = new WorksRequest();
        worksRequest.setTitle("Updated Work Title");
        worksRequest.setDescription("Updated Work Description");
        worksRequest.setImageUrl("https://example.com/updated-image.jpg");

        HttpEntity<WorksRequest> requestEntity = new HttpEntity<>(worksRequest);

        ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Response.class);

        System.out.println("Update Work Response Status Code: " + response.getStatusCode());
        System.out.println("Update Work Response Body: " + response.getBody());
    }

    private static void testDeleteWork(RestTemplate restTemplate) {
        String url = BASE_URL + "/delete/1"; // 假设作品ID为1

        HttpEntity<?> requestEntity = new HttpEntity<>(HttpHeaders.EMPTY);

        ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Response.class);

        System.out.println("Delete Work Response Status Code: " + response.getStatusCode());
        System.out.println("Delete Work Response Body: " + response.getBody());
    }

    private static void testGetAllWorks(RestTemplate restTemplate) {
        String url = BASE_URL + "/findAll";

        ResponseEntity<List<Works>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Works>>() {});

        System.out.println("Get All Works Response Status Code: " + response.getStatusCode());
        System.out.println("Get All Works Response Body: " + response.getBody());
    }

    //这里要结合，获取用户关注列表来一起使用，不过目前先分模块测试
    private static void testGetByFollowing(RestTemplate restTemplate) {
        String url = BASE_URL + "/get-by-following";

        Long[] followingUserIds = {2L, 3L}; // 假设关注的用户ID

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // 正确的方式：将每个 followingUserId 单独添加到参数中
        for (Long userId : followingUserIds) {
            params.add("followingUserIds", userId.toString());
        }

        // 添加页码参数
        params.add("page", "0");

        ResponseEntity<Response> response = restTemplate.getForEntity(
                url + "?" + UriComponentsBuilder.fromHttpUrl("").queryParams(params).build().getQuery(),
                Response.class
        );

        System.out.println("Get By Following Response Status Code: " + response.getStatusCode());
        System.out.println("Get By Following Response Body: " + response.getBody());
    }

    private static void testGetLatestWorks(RestTemplate restTemplate) {
        String url = BASE_URL + "/get-latest";

        // 设置参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0"); // 默认页码为第0页

        ResponseEntity<Response<List<Works>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Response<List<Works>>>() {},
                params
        );

        // 获取响应体中的作品列表
        List<Works> worksList = response.getBody().getData();

        System.out.println("Get Latest Works Response Status Code: " + response.getStatusCode());
        System.out.println("Number of Latest Works Retrieved: " + worksList.size());
        System.out.println("Latest Works Details:");
        for (Works work : worksList) {
            System.out.println(work);
        }
    }

    private static void testGetPopularWorks(RestTemplate restTemplate) {
        String url = BASE_URL + "/get-popular";

        // 设置参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0"); // 默认页码为第0页

        ResponseEntity<Response<List<Works>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Response<List<Works>>>() {},
                params
        );

        // 获取响应体中的作品列表
        List<Works> worksList = response.getBody().getData();

        System.out.println("Get Popular Works Response Status Code: " + response.getStatusCode());
        System.out.println("Number of Popular Works Retrieved: " + worksList.size());
        System.out.println("Popular Works Details:");
        for (Works work : worksList) {
            System.out.println(work);
        }
    }

    private static void testGetComprehensiveWorks(RestTemplate restTemplate) {
        String url = BASE_URL + "/get-comprehensive";

        // 设置参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "0"); // 默认页码为第0页

        ResponseEntity<Response<List<Works>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Response<List<Works>>>() {},
                params
        );

        // 获取响应体中的作品列表
        List<Works> worksList = response.getBody().getData();

        System.out.println("Get Comprehensive Works Response Status Code: " + response.getStatusCode());
        System.out.println("Number of Comprehensive Works Retrieved: " + worksList.size());
        System.out.println("Comprehensive Works Details:");
        for (Works work : worksList) {
            System.out.println(work);
        }
    }
}