package com.example.help_me_chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.help_me_chat.entity.ChatMessage;
import com.example.help_me_chat.entity.ChatReadPosition;

import java.util.List;

public interface ChatService extends IService<ChatMessage> {
    // 发送消息
    boolean sendMessage(ChatMessage message);

    // 获取聊天记录
    List<ChatMessage> getChatHistory(String friendId, long lastTimestamp);

    // 获取未读消息
    List<ChatMessage> getUnreadMessages(String friendId, long lastTimestamp);

    // 更新阅读位置
    boolean updateReadPosition(String friendId, String userId, int readMsgId);
}