package com.example.help_me_chat.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传 Service 接口（独立功能，便于扩展）
 */
public interface FileUploadService {

    /**
     * 上传聊天图片（本地存储）
     * @param file 前端上传的图片文件
     * @return 前端可访问的图片 URL
     */
    String uploadChatImage(MultipartFile file);
}