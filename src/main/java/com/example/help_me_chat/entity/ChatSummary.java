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
}