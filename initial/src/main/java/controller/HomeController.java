package controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    // 处理 GET 请求
    @GetMapping("/")
    public String home() {
        return "Welcome to the Home Page!";
    }

    // 处理 GET 请求，带参数
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello, %s!", name);
    }

    // 处理 POST 请求
    @PostMapping("/submit")
    public String submit() {
        return "Form submitted successfully!";
    }
}