package com.example.help_me_chat.entity;


import lombok.Data;

/**
 * 聊天列表摘要（前端本地使用，无需对齐后端）
 */
@Data
public class ChatSummary {
    private String name;
    private String lastMessage;
    private String time;
    private int avatarResId;
    private String avatarUrl;
    private String friendId;
    private boolean isFriendRequest;
    private Long unreadCount;

    public ChatSummary(String name, String lastMessage, String time, int avatarResId) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.avatarResId = avatarResId;
        this.isFriendRequest = false;
    }

    public ChatSummary(String name, String lastMessage, String time, String avatarUrl) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.avatarUrl = avatarUrl;
        this.isFriendRequest = false;
    }

    // 用于数据库查询结果的构造函数
    public ChatSummary(String friendId, String name, String lastMessage, Long lastMessageTime, Long unreadCount) {
        this.friendId = friendId;
        this.name = name;
        this.lastMessage = lastMessage;
        // 将时间戳转换为可读格式
        if (lastMessageTime != null) {
            this.time = String.valueOf(lastMessageTime);
        }
        this.unreadCount = unreadCount;
        this.isFriendRequest = false;
    }
}