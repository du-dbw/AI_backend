package com.example.backend.dto;

public class Response<T> {
    private int code;
    private String message;
    private T data;

    // 无参构造函数
    public Response() {
    }

    // 有参构造函数（不带数据）
    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // 有参构造函数（带数据）
    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
                "status=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}