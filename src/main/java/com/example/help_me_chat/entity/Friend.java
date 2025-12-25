package com.example.help_me_chat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 好友实体（对应前端Friend类 + 数据库friends表）
 */
@Data
@TableName("friends")
public class Friend implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("my_id") // 复合主键1
    private String myId;         // 当前登录用户ID（前端：myId）

    @TableField("friend_id") // 复合主键2（MyBatis-Plus需在配置中设置复合主键）
    private String friendId;     // 好友ID（前端：friendId）

    @TableField("friend_nickname")
    private String friendNickname; // 好友备注（前端：friendNickname）

    @TableField("friend_avatar")
    private String friendAvatar;   // 好友头像缓存（前端：friendAvatar）

    @TableField("is_auto_reply")
    private Boolean isAutoReply;   // 是否AI托管（前端：isAutoReply）
}