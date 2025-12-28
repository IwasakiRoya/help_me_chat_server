package com.example.help_me_chat.controller;

import com.example.help_me_chat.common.BaseResponse;
import com.example.help_me_chat.entity.ChatMessage;
import com.example.help_me_chat.entity.ChatSummary;
import com.example.help_me_chat.entity.User;
import com.example.help_me_chat.service.ChatService;
import com.example.help_me_chat.util.AuthUtil;
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

    @Autowired
    private AuthUtil authUtil;

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

    // ChatController.java（无需修改接口，仅确保 Service 层逻辑正确）
    @GetMapping("/history")
    public BaseResponse<List<ChatMessage>> getChatHistory(
            @RequestHeader("Authorization") String token,
            @RequestParam("friendId") String friendId,
            @RequestParam("lastTimestamp") long lastTimestamp) {
        User user = authUtil.getUserByToken(token);
        if (user == null) {
            return BaseResponse.error("用户未登录或Token无效");
        }
        // 调用修正后的 Service 方法
        List<ChatMessage> list = chatService.getChatHistory(user.getUserId(), friendId, lastTimestamp);
        return BaseResponse.success(list);
    }

    // 更新阅读位置
    @PostMapping("/read")
    public BaseResponse<Void> updateReadPosition(
            @RequestHeader("Authorization") String token,
            @RequestParam("friendId") String friendId,
            @RequestParam("readMsgId") int readMsgId) {
        User user = authUtil.getUserByToken(token);
        if (user == null) {
            return BaseResponse.error("用户未登录或Token无效");
        }
        boolean success = chatService.updateReadPosition(friendId, user.getUserId(), readMsgId);
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
        User user = authUtil.getUserByToken(token);
        if (user == null) {
            return BaseResponse.error("用户未登录或Token无效");
        }
        List<ChatMessage> list = chatService.getUnreadMessages(user.getUserId(), lastTimestamp);
        return BaseResponse.success(list);
    }

    // 获取聊天列表
    @GetMapping("/list")
    public BaseResponse<List<ChatSummary>> getChatList(@RequestHeader("Authorization") String token) {
        User user = authUtil.getUserByToken(token);
        if (user == null) {
            return BaseResponse.error("用户未登录或Token无效");
        }
        List<ChatSummary> list = chatService.getChatList(user.getUserId());
        return BaseResponse.success(list);
    }
}