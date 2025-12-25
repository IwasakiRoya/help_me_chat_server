package com.example.help_me_chat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体（对应前端User类 + 数据库users表）
 */
@Data
@TableName("users") // 映射数据库表名
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("user_id") // 主键映射
    private String userId;       // 唯一标识（前端：userId）

    @TableField("username")
    private String username;     // 登录账号（前端：username）

    @TableField("password")
    private String password;     // 加密密码（前端本地不存，仅后端用）

    @TableField("phone_number")
    private Long phoneNumber;    // 手机号（前端：phoneNumber）

    @TableField("nickname")
    private String nickname;     // 昵称（前端：nickname）

    @TableField("avatar_url")
    private String avatarUrl;    // 头像地址（前端：avatarUrl）

    @TableField("signature")
    private String signature;    // 个性签名（前端：signature）

    @TableField("ai_prompt")
    private String aiPrompt;     // AI人设（前端：aiPrompt）

    @TableField("api_key")
    private String apiKey;       // AI接口Key（前端：apiKey）

    @TableField("ai_model")
    private String aiModel;      // AI模型（前端：aiModel）

    @TableField("token")
    private String token;        // 登录Token（前端：token）

    @TableField("last_login_time")
    private Long lastLoginTime;  // 上次登录时间戳（前端：lastLoginTime）
}