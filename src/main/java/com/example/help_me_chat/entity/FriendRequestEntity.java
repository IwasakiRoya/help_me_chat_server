package com.example.help_me_chat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 好友请求实体（数据库friend_requests表）
 */
@Data
@TableName("friend_requests")
public class FriendRequestEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("request_id")
    private String requestId;     // 请求ID（UUID）

    @TableField("from_user_id")
    private String fromUserId;    // 发起者ID

    @TableField("to_user_id")
    private String toUserId;      // 接收者ID

    @TableField("request_msg")
    private String requestMsg;    // 请求备注

    @TableField("status")
    private Integer status;       // 状态（0=未处理，1=同意，2=拒绝）

    @TableField("create_time")
    private Date createTime;      // 发起时间

    @TableField("handle_time")
    private Date handleTime;      // 处理时间

    // 状态常量
    public static final int STATUS_UNHANDLED = 0;
    public static final int STATUS_AGREE = 1;
    public static final int STATUS_REJECT = 2;
}