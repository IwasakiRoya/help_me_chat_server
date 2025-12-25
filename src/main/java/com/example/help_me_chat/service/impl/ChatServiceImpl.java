package com.example.help_me_chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.help_me_chat.entity.ChatMessage;
import com.example.help_me_chat.entity.ChatReadPosition;
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
        return chatMessageMapper.insert(message) > 0;
    }

    @Override
    public List<ChatMessage> getChatHistory(String friendId, long lastTimestamp) {
        return chatMessageMapper.selectHistoryByFriendId(friendId, lastTimestamp);
    }

    @Override
    public List<ChatMessage> getUnreadMessages(String friendId, long lastTimestamp) {
        return chatMessageMapper.selectUnreadMessages(friendId, lastTimestamp);
    }

    @Override
    public boolean updateReadPosition(String friendId, String userId, int readMsgId) {
        ChatReadPosition position = chatReadPositionMapper.selectByFriendId(friendId, userId);
        if (position == null) {
            position = new ChatReadPosition();
            position.setFriendId(friendId);
            position.setUserId(userId);
        }
        position.setLastReadMsgId(readMsgId);
        position.setLastReadTime(System.currentTimeMillis());

        // 核心修改：调用重命名后的upsert方法
        int affectedRows = chatReadPositionMapper.upsert(position);
        return affectedRows > 0;
    }
}