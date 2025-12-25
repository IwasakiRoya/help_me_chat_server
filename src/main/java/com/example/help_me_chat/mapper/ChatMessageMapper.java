package com.example.help_me_chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.help_me_chat.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
    // 增量查询聊天记录
    @Select("SELECT * FROM messages WHERE friend_id = #{friendId} AND timestamp > #{lastTimestamp} ORDER BY timestamp ASC")
    List<ChatMessage> selectHistoryByFriendId(@Param("friendId") String friendId, @Param("lastTimestamp") long lastTimestamp);

    // 查询未读消息
    @Select("SELECT * FROM messages WHERE friend_id = #{friendId} AND status = 0 AND timestamp > #{lastTimestamp}")
    List<ChatMessage> selectUnreadMessages(@Param("friendId") String friendId, @Param("lastTimestamp") long lastTimestamp);
}