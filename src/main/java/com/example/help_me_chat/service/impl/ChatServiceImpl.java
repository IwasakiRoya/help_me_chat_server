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
        return chatMessageMapper.insert(message) > 0;
    }

    @Override
    public List<ChatMessage> getChatHistory(String userId, String friendId, long lastTimestamp) {
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
        }
        position.setLastReadMsgId(readMsgId);
        position.setLastReadTime(System.currentTimeMillis());

        // 核心修改：调用重命名后的upsert方法
        int affectedRows = chatReadPositionMapper.upsert(position);
        return affectedRows > 0;
    }

    @Override
    public List<ChatSummary> getChatList(String userId) {
        // 临时实现：获取用户的所有好友聊天记录摘要
        // 这里需要查询用户与所有好友的最后一条消息，以及未读消息数量
        // 为了简化，先返回模拟数据
        return chatMessageMapper.selectChatSummaries(userId);
    }

    // 新增：查询所有聊天记录（MyBatis 对应方法）
    public List<ChatMessage> selectAllHistoryBetweenUsers(String userId, String friendId) {
        return chatMessageMapper.selectAllHistoryBetweenUsers(userId, friendId);
    }
}