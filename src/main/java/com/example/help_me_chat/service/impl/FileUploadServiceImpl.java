package com.example.help_me_chat.service.impl;

import com.example.help_me_chat.service.FileUploadService;
import com.example.help_me_chat.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传 Service 实现类（本地存储实现）
 */
@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    // 注入你提供的文件上传工具类
    private final FileUploadUtil fileUploadUtil;

    /**
     * 上传聊天图片（实现本地存储逻辑）
     */
    @Override
    public String uploadChatImage(MultipartFile file) {
        try {
            // 调用工具类完成文件上传，返回可访问 URL
            return fileUploadUtil.uploadImage(file);
        } catch (IOException e) {
            throw new RuntimeException("文件写入失败：" + e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}