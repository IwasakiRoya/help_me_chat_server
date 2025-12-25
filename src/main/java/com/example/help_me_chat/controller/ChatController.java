package com.example.help_me_chat.controller;

import com.example.help_me_chat.common.BaseResponse;
import com.example.help_me_chat.entity.ChatMessage;
import com.example.help_me_chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聊天相关接口（对齐前端ApiService的chat接口）
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // 发送消息
    @PostMapping("/send")
    public BaseResponse<ChatMessage> sendMessage(@RequestHeader("Authorization") String token, @RequestBody ChatMessage message) {
        boolean success = chatService.sendMessage(message);
        if (success) {
            return BaseResponse.success(message);
        } else {
            return BaseResponse.error("发送消息失败");
        }
    }

    // 获取聊天记录
    @GetMapping("/history")
    public BaseResponse<List<ChatMessage>> getChatHistory(
            @RequestHeader("Authorization") String token,
            @RequestParam("friendId") String friendId,
            @RequestParam("lastTimestamp") long lastTimestamp) {
        List<ChatMessage> list = chatService.getChatHistory(friendId, lastTimestamp);
        return BaseResponse.success(list);
    }

    // 更新阅读位置
    @PostMapping("/read")
    public BaseResponse<Void> updateReadPosition(
            @RequestHeader("Authorization") String token,
            @RequestParam("friendId") String friendId,
            @RequestParam("readMsgId") int readMsgId) {
        String userId = "1000"; // 临时
        boolean success = chatService.updateReadPosition(friendId, userId, readMsgId);
        if (success) {
            return BaseResponse.success();
        } else {
            return BaseResponse.error("更新阅读位置失败");
        }
    }

    // 获取未读消息
    @GetMapping("/messages/unread")
    public BaseResponse<List<ChatMessage>> getUnreadMessages(
            @RequestHeader("Authorization") String token,
            @RequestParam("lastTimestamp") long lastTimestamp) {
        String friendId = "1001"; // 临时
        List<ChatMessage> list = chatService.getUnreadMessages(friendId, lastTimestamp);
        return BaseResponse.success(list);
    }
}