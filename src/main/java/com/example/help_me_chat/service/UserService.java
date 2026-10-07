package com.example.help_me_chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.help_me_chat.entity.User;
import com.example.help_me_chat.model.request.ChangePwdRequest;

public interface UserService extends IService<User> {
    // 登录
    User login(String username, String password);

    // 注册
    boolean register(String username, String password);

    // 修改密码
    boolean changePassword(ChangePwdRequest request);

    // 搜索用户
    User searchUser(String keyword);

    // 更新用户信息
    boolean updateUserInfo(User user);

    // 根据Token查询用户
    User getUserByToken(String token);
}