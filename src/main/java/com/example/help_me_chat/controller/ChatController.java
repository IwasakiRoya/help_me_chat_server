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


    // 核心修复：添加 produces = "application/json;charset=UTF-8"
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public BaseResponse<List<ChatSummary>> getChatList(@RequestHeader("Authorization") String token) {
        User user = authUtil.getUserByToken(token);
        if (user == null) {
            return BaseResponse.error("用户未登录或Token无效");
        }
        List<ChatSummary> list = chatService.getChatList(user.getUserId());
        return BaseResponse.success(list);
    }

    /**
     * 标记指定时间戳之前的所有消息为已读（核心接口）
     * @param token 用户令牌
     * @param friendId 好友ID
     * @param lastRenderedTimestamp 前端最新已渲染消息时间戳
     * @return 标记结果
     */
    @GetMapping("/markAsRead")
    public BaseResponse<Void> markChatMessagesAsRead(
            @RequestHeader("Authorization") String token,
            @RequestParam("friendId") String friendId,
            @RequestParam("lastRenderedTimestamp") long lastRenderedTimestamp
    ) {
        // 1. 校验用户登录状态
        User user = authUtil.getUserByToken(token);
        if (user == null) {
            return BaseResponse.error("用户未登录或Token失效");
        }

        // 2. 调用Service层标记已读
        boolean success = chatService.markMessagesAsRead(user.getUserId(), friendId, lastRenderedTimestamp);
        if (success) {
            return BaseResponse.success();
        } else {
            return BaseResponse.error("标记已读失败");
        }
    }
}