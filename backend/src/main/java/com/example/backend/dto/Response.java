package com.example.backend.dto;

import java.util.List;

public class Response<T> {
    private int status;
    private String message;
    private T data;

    // 无参构造函数
    public Response() {
    }

    // 有参构造函数（不带数据）
    public Response(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // 有参构造函数（带数据）
    public Response(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}