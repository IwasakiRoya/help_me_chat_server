package com.example.help_me_chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.help_me_chat.entity.ChatMessage;
import com.example.help_me_chat.entity.ChatReadPosition;
import com.example.help_me_chat.entity.ChatSummary;
import com.example.help_me_chat.mapper.ChatMessageMapper;
import com.example.help_me_chat.mapper.ChatReadPositionMapper;
import com.example.help_me_chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatService {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Autowired
    private ChatReadPositionMapper chatReadPositionMapper;

    @Override
    public boolean sendMessage(ChatMessage message) {
        message.setTimestamp(System.currentTimeMillis());
        message.setStatus(0); // 成功
        // 核心修复：确保发送消息时，friendId和userId正确设置（避免前端查询不到）
        if (message.getFriendId() == null || message.getUserId() == null) {
            return false;
        }
        return chatMessageMapper.insert(message) > 0;
    }

    @Override
    public List<ChatMessage> getChatHistory(String userId, String friendId, long lastTimestamp) {
        // 核心修复：确保查询的是两个用户之间的消息，且时间戳筛选正确
        return chatMessageMapper.selectHistoryBetweenUsers(userId, friendId, lastTimestamp);
    }

    @Override
    public List<ChatMessage> getUnreadMessages(String userId, long lastTimestamp) {
        return chatMessageMapper.selectUnreadMessagesForUser(userId, lastTimestamp);
    }

    @Override
    public boolean updateReadPosition(String friendId, String userId, int readMsgId) {
        ChatReadPosition position = chatReadPositionMapper.selectByFriendId(friendId, userId);
        if (position == null) {
            position = new ChatReadPosition();
            position.setFriendId(friendId);
            position.setUserId(userId);
            // 兜底：初始化lastReadTime，避免为null
            position.setLastReadTime(System.currentTimeMillis());
        }
        position.setLastReadMsgId(readMsgId);
        position.setLastReadTime(System.currentTimeMillis());

        // 调用重命名后的upsert方法
        int affectedRows = chatReadPositionMapper.upsert(position);
        return affectedRows > 0;
    }

    @Override
    public List<ChatSummary> getChatList(String userId) {
        // 核心修复：确保查询的聊天摘要包含未读消息数和正确的时间戳
        List<ChatSummary> chatSummaries = chatMessageMapper.selectChatSummaries(userId);
        // 兜底：如果查询结果为空，返回空列表而非null
        return chatSummaries != null ? chatSummaries : List.of();
    }

    // 新增：查询所有聊天记录（MyBatis 对应方法）
    public List<ChatMessage> selectAllHistoryBetweenUsers(String userId, String friendId) {
        return chatMessageMapper.selectAllHistoryBetweenUsers(userId, friendId);
    }

    /**
     * 核心实现：更新 ChatReadPosition 的 last_read_time 为最新已渲染时间戳
     * 后续查询未读数时，会以该时间戳为临界点，仅统计之后的消息
     */
    @Override
    public boolean markMessagesAsRead(String userId, String friendId, long lastRenderedTimestamp) {
        if (userId == null || userId.isEmpty() || friendId == null || friendId.isEmpty() || lastRenderedTimestamp <= 0) {
            return false;
        }

        // 1. 查询当前用户与好友的阅读位置记录
        ChatReadPosition position = chatReadPositionMapper.selectByFriendId(friendId, userId);
        if (position == null) {
            position = new ChatReadPosition();
            position.setFriendId(friendId);
            position.setUserId(userId);
        }

        // 2. 更新阅读位置：last_read_time 设为最新已渲染时间戳（核心临界点）
        position.setLastReadTime(lastRenderedTimestamp);
        // 可选：更新 last_read_msg_id（若需要按消息ID标记）
        // position.setLastReadMsgId(lastRenderedMsgId);

        // 3. 调用 upsert 方法（插入/更新），确保记录唯一
        int affectedRows = chatReadPositionMapper.upsert(position);
        return affectedRows > 0;
    }
}