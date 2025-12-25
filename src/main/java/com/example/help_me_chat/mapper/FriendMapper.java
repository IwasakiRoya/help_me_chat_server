package com.example.help_me_chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.help_me_chat.entity.Friend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendMapper extends BaseMapper<Friend> {
    // 根据当前用户ID查询好友列表
    @Select("SELECT * FROM friends WHERE my_id = #{myId}")
    List<Friend> selectByMyId(@Param("myId") String myId);

    // 检查好友关系是否存在
    @Select("SELECT COUNT(*) FROM friends WHERE my_id = #{myId} AND friend_id = #{friendId}")
    int countFriend(@Param("myId") String myId, @Param("friendId") String friendId);
}