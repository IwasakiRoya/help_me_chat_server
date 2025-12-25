package com.example.help_me_chat.model.request;

import lombok.Data;

@Data
public class FriendRequest {
    private String fromUserId;
    private String toUserId;
    private String requestMsg;
}