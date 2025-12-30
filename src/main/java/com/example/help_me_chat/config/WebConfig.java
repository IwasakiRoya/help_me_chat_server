package com.example.help_me_chat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * Web配置类：静态资源映射（支持前端访问项目根目录下的上传图片）
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 从配置文件读取项目相对路径
    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 配置静态资源映射规则
     * 前端访问 http://localhost:8080/images/xxx.jpg -> 映射到 项目根目录/uploads/chat/images/xxx.jpg
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. 获取项目根目录
        String projectRootPath = System.getProperty("user.dir");
        // 2. 转换为：项目根目录 + 配置中的相对路径（确保指向项目下的 uploads 文件夹）
        String absoluteUploadPath = new File(projectRootPath, uploadPath).getAbsolutePath() + File.separator;
        // 3. 配置资源映射（必须加 "file:" 前缀，表示本地文件系统）
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + absoluteUploadPath);
    }
}