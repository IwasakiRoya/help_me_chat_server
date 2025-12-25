package com.example.help_me_chat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 聊天消息实体（对应前端ChatMessage类 + 数据库messages表）
 */
@Data
@TableName("messages")
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO) // 自增主键
    private Integer id;          // 消息ID（前端：id）

    @TableField("friend_id")
    private String friendId;     // 好友ID（前端：friendId）

    @TableField("content")
    private String content;      // 消息内容（前端：content）

    @TableField("type")
    private Integer type;        // 消息类型（0=接收，1=发送；前端：type）

    @TableField("timestamp")
    private Long timestamp;      // 时间戳（前端：timestamp）

    @TableField("status")
    private Integer status;      // 消息状态（0=成功，1=思考中，2=失败；前端：status）

    @TableField("user_id")
    private String userId;       // 发送者ID（前端未定义，但后端需要）

    // 前端常量映射
    public static final int TYPE_SENT = 1;
    public static final int TYPE_RECEIVED = 0;
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_THINKING = 1;
    public static final int STATUS_FAILED = 2;
}