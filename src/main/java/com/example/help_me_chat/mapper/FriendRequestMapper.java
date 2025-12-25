package com.example.help_me_chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.help_me_chat.entity.FriendRequestEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendRequestMapper extends BaseMapper<FriendRequestEntity> {
    // 根据接收者ID查询未处理的好友请求
    @Select("SELECT * FROM friend_requests WHERE to_user_id = #{toUserId} AND status = 0")
    List<FriendRequestEntity> selectByToUserId(@Param("toUserId") String toUserId);
}