package com.example.help_me_chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.help_me_chat.entity.ChatReadPosition;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ChatReadPositionMapper extends BaseMapper<ChatReadPosition> {
    // 根据好友ID+用户ID查询阅读位置
    @Select("SELECT * FROM chat_read_position WHERE friend_id = #{friendId} AND user_id = #{userId}")
    ChatReadPosition selectByFriendId(@Param("friendId") String friendId, @Param("userId") String userId);

    // 核心修改：重命名方法为 upsert（避免和BaseMapper的insertOrUpdate冲突）
    @Insert("INSERT INTO chat_read_position (friend_id, user_id, last_read_msg_id, last_read_time) " +
            "VALUES (#{friendId}, #{userId}, #{lastReadMsgId}, #{lastReadTime}) " +
            "ON DUPLICATE KEY UPDATE " +
            "last_read_msg_id = #{lastReadMsgId}, " +
            "last_read_time = #{lastReadTime}")
    int upsert(ChatReadPosition position); // 方法名改为upsert
}