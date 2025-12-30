package com.example.help_me_chat.controller;

import com.example.help_me_chat.common.BaseResponse;
import com.example.help_me_chat.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传 Controller（独立模块，对齐前端接口）
 */
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileUploadController {

    // 注入文件上传 Service
    private final FileUploadService fileUploadService;

    /**
     * 图片上传接口（完全对齐前端 ApiService.uploadFile）
     * 前端请求：POST /api/file/upload，MultipartFile file，请求头 Authorization
     * 后端响应：BaseResponse<String>（data 为图片访问 URL）
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(
            @RequestHeader(required = false) String Authorization, // 兼容前端传递的 token（暂不校验，后续可扩展）
            @RequestParam("file") MultipartFile file // 对应前端 MultipartBody.Part("file")
    ) {
        System.out.println(Authorization + "上传作业启动--------------------------------------------");
        // 1. 校验文件是否为空
        if (file == null || file.isEmpty()) {
            System.out.println("上传的文件不能为空");
            return BaseResponse.error("上传的文件不能为空");
        }

        try {
            System.out.println("正在上传文件：" + file.getOriginalFilename());
            // 2. 调用 Service 完成文件上传
            String imageUrl = fileUploadService.uploadChatImage(file);
            // 3. 返回成功响应（包含图片 URL）
            return BaseResponse.success(imageUrl);
        } catch (RuntimeException e) {
            System.out.println("上传文件失败：" + e.getMessage());
            // 4. 捕获异常，返回错误响应
            return BaseResponse.error(e.getMessage());
        }
    }
}