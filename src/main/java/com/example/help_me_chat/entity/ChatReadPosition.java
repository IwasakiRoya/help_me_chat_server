package com.example.help_me_chat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("chat_read_position") // 仅保留表名注解，移除 @UniqueIndex
public class ChatReadPosition implements Serializable {
    private static final long serialVersionUID = 1L;

    // 无单主键，仅保留业务字段（user_id + friend_id 联合唯一通过SQL建立）
    @TableField("friend_id")
    private String friendId;       // 好友ID（前端：friendId）

    @TableField("last_read_msg_id")
    private Integer lastReadMsgId; // 最后阅读消息ID（前端：lastReadMsgId）

    @TableField("last_read_time")
    private Long lastReadTime;     // 最后阅读时间戳（前端：lastReadTime）

    @TableField("user_id")
    private String userId;         // 所属用户ID

    // 无参构造
    public ChatReadPosition() {
        this.friendId = "";
        this.lastReadMsgId = 0;
        this.lastReadTime = System.currentTimeMillis();
        this.userId = ""; // 初始化
    }

    // 有参构造
    public ChatReadPosition(String friendId, String userId, int lastReadMsgId, long lastReadTime) {
        this.friendId = friendId;
        this.userId = userId;
        this.lastReadMsgId = lastReadMsgId;
        this.lastReadTime = lastReadTime;
    }
}