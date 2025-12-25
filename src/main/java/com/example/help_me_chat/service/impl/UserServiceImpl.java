package com.example.help_me_chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.help_me_chat.entity.User;
import com.example.help_me_chat.mapper.UserMapper;
import com.example.help_me_chat.model.request.ChangePwdRequest;
import com.example.help_me_chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    // BCrypt加密
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User login(String username, String password) {
        // 查询用户
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return null;
        }
        // 验证密码
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public boolean register(String username, String password) {
        // 检查用户名是否已存在
        if (userMapper.selectByUsername(username) != null) {
            return false;
        }
        // 创建用户
        User user = new User();
        user.setUserId(String.valueOf(System.currentTimeMillis())); // 临时生成用户ID
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // 加密密码
        user.setNickname("新用户" + System.currentTimeMillis() % 10000);
        return userMapper.insert(user) > 0;
    }

    @Override
    public boolean changePassword(ChangePwdRequest request) {
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            return false;
        }
        // 验证旧密码
        if (!passwordEncoder.matches(request.getOldPwd(), user.getPassword())) {
            return false;
        }
        // 更新新密码
        user.setPassword(passwordEncoder.encode(request.getNewPwd()));
        return userMapper.updateById(user) > 0;
    }

    @Override
    public User searchUser(String keyword) {
        return userMapper.selectByKeyword(keyword);
    }

    @Override
    public boolean updateUserInfo(User user) {
        return userMapper.updateById(user) > 0;
    }
}