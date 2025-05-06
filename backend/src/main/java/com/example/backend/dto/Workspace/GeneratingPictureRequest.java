package com.example.backend.dto.Workspace;

public class GeneratingPictureRequest {
    private String rawImageUrl; // 原始图片URL
    private String templateImageUrl; // 模板图片URL
    private String inputText; // 用户输入的文字
    private Long workspaceId; // 工作空间ID

    // 无参构造函数
    public GeneratingPictureRequest() {
    }

    // 全参构造函数
    public GeneratingPictureRequest(String rawImageUrl, String templateImageUrl, String inputText, Long workspaceId) {
        this.rawImageUrl = rawImageUrl;
        this.templateImageUrl = templateImageUrl;
        this.inputText = inputText;
        this.workspaceId = workspaceId;
    }

    // Getters and Setters
    public String getRawImageUrl() {
        return rawImageUrl;
    }

    public void setRawImageUrl(String rawImageUrl) {
        this.rawImageUrl = rawImageUrl;
    }

    public String getTemplateImageUrl() {
        return templateImageUrl;
    }

    public void setTemplateImageUrl(String templateImageUrl) {
        this.templateImageUrl = templateImageUrl;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }
}