package com.example.help_me_chat.model.request;

import lombok.Data;

@Data
public class HandleFriendRequest {
    private String requestId;
    private int status; // 1=同意，2=拒绝
}