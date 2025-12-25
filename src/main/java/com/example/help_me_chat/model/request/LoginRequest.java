package com.example.help_me_chat.model.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}