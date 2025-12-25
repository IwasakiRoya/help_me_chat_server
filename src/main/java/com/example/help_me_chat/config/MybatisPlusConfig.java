package com.example.help_me_chat.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 配置类（适配 Spring Boot 4.0 + 移除分页功能）
 * 核心功能：Mapper扫描 + 事务管理 + 时间字段自动填充
 */
@Configuration
@MapperScan(basePackages = "com.example.help_me_chat.mapper") // 扫描Mapper接口
@EnableTransactionManagement(proxyTargetClass = true) // 开启事务管理（可选但推荐）
public class MybatisPlusConfig {

    /**
     * 元对象处理器（核心保留：自动填充创建/更新时间）
     * 解决实体类中create_time/update_time字段自动填充问题
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                // 新增时填充创建时间和更新时间
                strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
                strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
                // 兼容前端时间戳字段（如lastLoginTime）
                strictInsertFill(metaObject, "lastLoginTime", Long.class, System.currentTimeMillis());
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                // 更新时仅填充更新时间
                strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }
        };
    }
}