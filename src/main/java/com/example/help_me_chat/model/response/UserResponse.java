package com.example.help_me_chat.model.response;

import com.example.help_me_chat.entity.User;
import lombok.Data;

@Data
public class UserResponse {
    private int code;
    private String message;
    private User data;
}