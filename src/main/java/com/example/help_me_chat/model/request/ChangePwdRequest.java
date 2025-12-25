package com.example.help_me_chat.model.request;

import lombok.Data;

@Data
public class ChangePwdRequest {
    private String userId;
    private String oldPwd;
    private String newPwd;
}