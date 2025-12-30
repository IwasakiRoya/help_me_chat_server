package com.example.help_me_chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.help_me_chat.entity.ChatMessage;
import com.example.help_me_chat.entity.ChatSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 聊天消息 Mapper 接口（最终版，包含所有修复点）
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    /**
     * 查询历史更早的消息（用于下拉加载更多）
     * @param userId 当前用户ID
     * @param friendId 好友ID
     * @param lastTimestamp 最后一条消息时间戳
     * @param pageSize 每页条数
     * @return 历史消息列表
     */
    @Select("SELECT id, friend_id, content, type, timestamp, status, user_id, msg_type FROM messages WHERE ((user_id = #{userId} AND friend_id = #{friendId}) OR (user_id = #{friendId} AND friend_id = #{userId})) AND timestamp < #{lastTimestamp} ORDER BY timestamp DESC LIMIT #{pageSize}")
    List<ChatMessage> selectHistoryBeforeTimestamp(
            @Param("userId") String userId,
            @Param("friendId") String friendId,
            @Param("lastTimestamp") long lastTimestamp,
            @Param("pageSize") int pageSize);

    /**
     * 拉取新消息（用于轮询刷新、实时更新）
     * @param userId 当前用户ID
     * @param friendId 好友ID
     * @param lastTimestamp 最后一次拉取的时间戳
     * @return 新增消息列表
     */
    @Select("SELECT id, friend_id, content, type, timestamp, status, user_id, msg_type FROM messages WHERE ((user_id = #{userId} AND friend_id = #{friendId}) OR (user_id = #{friendId} AND friend_id = #{userId})) AND timestamp > #{lastTimestamp} ORDER BY timestamp ASC")
    List<ChatMessage> selectHistoryBetweenUsers(
            @Param("userId") String userId,
            @Param("friendId") String friendId,
            @Param("lastTimestamp") long lastTimestamp);

    /**
     * 查询两个用户之间的所有聊天记录
     * @param userId 当前用户ID
     * @param friendId 好友ID
     * @return 完整聊天记录列表
     */
    @Select("SELECT id, friend_id, content, type, timestamp, status, user_id, msg_type FROM messages WHERE ((user_id = #{userId} AND friend_id = #{friendId}) OR (user_id = #{friendId} AND friend_id = #{userId})) ORDER BY timestamp ASC")
    List<ChatMessage> selectAllHistoryBetweenUsers(
            @Param("userId") String userId,
            @Param("friendId") String friendId);

    /**
     * 按好友ID增量查询聊天记录（兼容旧逻辑，建议优先使用 selectHistoryBetweenUsers）
     * @param friendId 好友ID
     * @param lastTimestamp 最后一次拉取的时间戳
     * @return 新增消息列表
     */
    @Select("SELECT id, friend_id, content, type, timestamp, status, user_id, msg_type FROM messages WHERE friend_id = #{friendId} AND timestamp > #{lastTimestamp} ORDER BY timestamp ASC")
    List<ChatMessage> selectHistoryByFriendId(
            @Param("friendId") String friendId,
            @Param("lastTimestamp") long lastTimestamp);

    /**
     * 查询用户的所有未读消息
     * @param userId 当前用户ID
     * @param lastTimestamp 最后阅读时间戳
     * @return 未读消息列表
     */
    @Select("SELECT id, friend_id, content, type, timestamp, status, user_id, msg_type FROM messages WHERE friend_id = #{userId} AND status = 0 AND timestamp > #{lastTimestamp}")
    List<ChatMessage> selectUnreadMessagesForUser(
            @Param("userId") String userId,
            @Param("lastTimestamp") long lastTimestamp);

    /**
     * 查询两个用户之间的未读消息
     * @param userId 当前用户ID
     * @param friendId 好友ID
     * @param lastTimestamp 最后阅读时间戳
     * @return 未读消息列表
     */
    @Select("SELECT id, friend_id, content, type, timestamp, status, user_id, msg_type FROM messages WHERE ((user_id = #{friendId} AND friend_id = #{userId}) OR (user_id = #{userId} AND friend_id = #{friendId})) AND status = 0 AND timestamp > #{lastTimestamp}")
    List<ChatMessage> selectUnreadMessagesBetweenUsers(
            @Param("userId") String userId,
            @Param("friendId") String friendId,
            @Param("lastTimestamp") long lastTimestamp);

    /**
     * 按好友ID查询未读消息（兼容旧逻辑，建议优先使用 selectUnreadMessagesBetweenUsers）
     * @param friendId 好友ID
     * @param lastTimestamp 最后阅读时间戳
     * @return 未读消息列表
     */
    @Select("SELECT id, friend_id, content, type, timestamp, status, user_id, msg_type FROM messages WHERE friend_id = #{friendId} AND status = 0 AND timestamp > #{lastTimestamp}")
    List<ChatMessage> selectUnreadMessages(
            @Param("friendId") String friendId,
            @Param("lastTimestamp") long lastTimestamp);

    /**
     * 查询聊天摘要列表（最终修复版）
     * 包含：好友ID、好友名称、最后一条消息、消息时间戳、未读消息数
     * 修复点：1. 左连接用户表 2. 昵称兜底 3. 仅统计对方发送的有效消息 4. 未读数关联阅读位置
     * @param userId 当前用户ID
     * @return 聊天摘要列表
     */
    @Select("SELECT " +
            "    other_user.other_user_id as friendId, " +
            "    IFNULL(u.nickname, other_user.other_user_id) as name, " +
            "    latest_msg.content as lastMessage, " +
            "    latest_msg.timestamp as lastMessageTime, " +
            "    (SELECT COUNT(*) FROM messages m2 " +
            "     WHERE m2.user_id = other_user.other_user_id " +
            "     AND m2.friend_id = #{userId} " +
            "     AND m2.status = 0 " +
            "     AND m2.timestamp > COALESCE((SELECT crp.last_read_time FROM chat_read_position crp WHERE crp.user_id = #{userId} AND crp.friend_id = other_user.other_user_id), 0)) as unreadCount " +
            "FROM " +
            "    (SELECT " +
            "        CASE " +
            "            WHEN user_id = #{userId} THEN friend_id " +
            "            ELSE user_id " +
            "        END as other_user_id, " +
            "        content, " +
            "        timestamp, " +
            "        id, " +
            "        msg_type " +
            "    FROM messages " +
            "    WHERE (user_id = #{userId} OR friend_id = #{userId}) " +
            "    ) latest_msg " +
            "JOIN " +
            "    (SELECT " +
            "        CASE " +
            "            WHEN user_id = #{userId} THEN friend_id " +
            "            ELSE user_id " +
            "        END as other_user_id, " +
            "        MAX(id) as max_id " +
            "    FROM messages " +
            "    WHERE (user_id = #{userId} OR friend_id = #{userId}) " +
            "    GROUP BY CASE " +
            "        WHEN user_id = #{userId} THEN friend_id " +
            "        ELSE user_id " +
            "    END " +
            "    ) other_user ON latest_msg.id = other_user.max_id " +
            "LEFT JOIN users u ON u.user_id = other_user.other_user_id " +
            "ORDER BY latest_msg.timestamp DESC")
    List<ChatSummary> selectChatSummaries(@Param("userId") String userId);
}