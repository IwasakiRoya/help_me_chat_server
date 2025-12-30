package com.example.help_me_chat.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUploadUtil {

    // 从配置文件读取存储路径（项目相对路径）
    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.access-prefix}")
    private String accessPrefix;

    /**
     * 上传图片并返回可访问的 URL（修改后：保存到项目根目录，支持 Git 同步）
     */
    public String uploadImage(MultipartFile file) throws IOException {
        // 1. 校验文件是否为空
        if (file.isEmpty()) {
            throw new RuntimeException("上传的文件不能为空");
        }

        // 2. 校验文件类型（只允许图片）
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new RuntimeException("文件格式非法");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!suffix.matches("\\.(jpg|jpeg|png|gif)$")) {
            throw new RuntimeException("仅支持 jpg/jpeg/png/gif 格式的图片");
        }

        // 3. 生成唯一文件名（避免重复覆盖）
        String fileName = UUID.randomUUID().toString() + suffix;

        // 4. 核心修改：获取项目根目录，转换为项目下的绝对路径（解决 Tomcat 临时目录问题）
        // 获取项目运行时根目录（即 pom.xml 所在目录，Git 仓库根目录）
        String projectRootPath = System.getProperty("user.dir");
        // 拼接：项目根目录 + 配置中的相对路径（./uploads/chat/images/）
        String absoluteUploadPath = new File(projectRootPath, uploadPath).getAbsolutePath();

        // 5. 关键：递归创建多级目录（若不存在），解决「系统找不到指定的路径」问题
        File destDir = new File(absoluteUploadPath);
        if (!destDir.exists()) {
            // mkdirs()：递归创建所有不存在的父目录（mkdir() 只能创建单级目录，会失败）
            boolean mkdirsSuccess = destDir.mkdirs();
            if (!mkdirsSuccess) {
                throw new RuntimeException("目录创建失败：" + absoluteUploadPath);
            }
        }

        // 6. 保存文件到「项目根目录下的 uploads/chat/images/」
        File destFile = new File(destDir, fileName);
        file.transferTo(destFile);

        // 7. 拼接并返回前端可访问的 URL
        return accessPrefix + fileName;
    }
}