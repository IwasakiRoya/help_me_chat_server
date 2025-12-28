package com.example.help_me_chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.help_me_chat.entity.ChatMessage;
import com.example.help_me_chat.entity.ChatSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
    // ChatMapper.java（新增方法，用于查询历史消息（更早的消息））
    @Select("SELECT * FROM messages WHERE ((user_id = #{userId} AND friend_id = #{friendId}) OR (user_id = #{friendId} AND friend_id = #{userId})) AND timestamp < #{lastTimestamp} ORDER BY timestamp DESC LIMIT #{pageSize}")
    List<ChatMessage> selectHistoryBeforeTimestamp(
            @Param("userId") String userId,
            @Param("friendId") String friendId,
            @Param("lastTimestamp") long lastTimestamp,
            @Param("pageSize") int pageSize);

    // 保留原有拉取新消息的方法（用于后续刷新新消息）
    @Select("SELECT * FROM messages WHERE ((user_id = #{userId} AND friend_id = #{friendId}) OR (user_id = #{friendId} AND friend_id = #{userId})) AND timestamp > #{lastTimestamp} ORDER BY timestamp ASC")
    List<ChatMessage> selectHistoryBetweenUsers(
            @Param("userId") String userId,
            @Param("friendId") String friendId,
            @Param("lastTimestamp") long lastTimestamp);

    // ChatMapper.java 新增：
    @Select("SELECT * FROM messages WHERE ((user_id = #{userId} AND friend_id = #{friendId}) OR (user_id = #{friendId} AND friend_id = #{userId})) ORDER BY timestamp ASC")
    List<ChatMessage> selectAllHistoryBetweenUsers(
            @Param("userId") String userId,
            @Param("friendId") String friendId);

    // 增量查询聊天记录（保留旧方法，但建议使用新方法）
    @Select("SELECT * FROM messages WHERE friend_id = #{friendId} AND timestamp > #{lastTimestamp} ORDER BY timestamp ASC")
    List<ChatMessage> selectHistoryByFriendId(@Param("friendId") String friendId, @Param("lastTimestamp") long lastTimestamp);

    // 查询用户的所有未读消息
    @Select("SELECT * FROM messages WHERE friend_id = #{userId} AND status = 0 AND timestamp > #{lastTimestamp}")
    List<ChatMessage> selectUnreadMessagesForUser(@Param("userId") String userId, @Param("lastTimestamp") long lastTimestamp);

    // 查询两个用户之间的未读消息
    @Select("SELECT * FROM messages WHERE ((user_id = #{friendId} AND friend_id = #{userId}) OR (user_id = #{userId} AND friend_id = #{friendId})) AND status = 0 AND timestamp > #{lastTimestamp}")
    List<ChatMessage> selectUnreadMessagesBetweenUsers(@Param("userId") String userId, @Param("friendId") String friendId, @Param("lastTimestamp") long lastTimestamp);

    // 查询未读消息（保留旧方法，但建议使用新方法）
    @Select("SELECT * FROM messages WHERE friend_id = #{friendId} AND status = 0 AND timestamp > #{lastTimestamp}")
    List<ChatMessage> selectUnreadMessages(@Param("friendId") String friendId, @Param("lastTimestamp") long lastTimestamp);

    // 查询聊天摘要列表
    @Select("SELECT " +
            "    other_user.other_user_id as friendId, " +
            "    u.nickname as name, " +
            "    latest_msg.content as lastMessage, " +
            "    latest_msg.timestamp as lastMessageTime, " +
            "    (SELECT COUNT(*) FROM messages m2 WHERE ((m2.user_id = #{userId} AND m2.friend_id = other_user.other_user_id) OR (m2.user_id = other_user.other_user_id AND m2.friend_id = #{userId})) " +
            "    AND m2.timestamp > COALESCE((SELECT last_read_time FROM chat_read_position WHERE user_id = #{userId} AND friend_id = other_user.other_user_id), 0)) as unreadCount " +
            "FROM " +
            "    (SELECT " +
            "        CASE " +
            "            WHEN user_id = #{userId} THEN friend_id " +
            "            ELSE user_id " +
            "        END as other_user_id, " +
            "        content, " +
            "        timestamp, " +
            "        id " +
            "    FROM messages " +
            "    WHERE user_id = #{userId} OR friend_id = #{userId} " +
            "    ) latest_msg " +
            "JOIN " +
            "    (SELECT " +
            "        CASE " +
            "            WHEN user_id = #{userId} THEN friend_id " +
            "            ELSE user_id " +
            "        END as other_user_id, " +
            "        MAX(id) as max_id " +
            "    FROM messages " +
            "    WHERE user_id = #{userId} OR friend_id = #{userId} " +
            "    GROUP BY CASE " +
            "        WHEN user_id = #{userId} THEN friend_id " +
            "        ELSE user_id " +
            "    END " +
            "    ) other_user ON latest_msg.id = other_user.max_id " +
            "JOIN users u ON u.user_id = other_user.other_user_id " +
            "ORDER BY latest_msg.timestamp DESC")
    List<ChatSummary> selectChatSummaries(@Param("userId") String userId);
}