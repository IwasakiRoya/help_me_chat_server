package com.example.help_me_chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.help_me_chat.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 根据用户名查询用户
    @Select("SELECT * FROM users WHERE username = #{username}")
    User selectByUsername(@Param("username") String username);

    // 根据手机号/昵称搜索用户
    @Select("SELECT * FROM users WHERE user_id = #{keyword} OR phone_number = #{keyword} OR nickname LIKE CONCAT('%',#{keyword},'%')")
    User selectByKeyword(@Param("keyword") String keyword);

    // 根据token查询用户
    @Select("SELECT * FROM users WHERE token = #{token}")
    User selectByToken(@Param("token") String token);
}