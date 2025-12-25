package com.example.help_me_chat.controller;

import com.example.help_me_chat.common.BaseResponse;
import com.example.help_me_chat.entity.User;
import com.example.help_me_chat.model.request.ChangePwdRequest;
import com.example.help_me_chat.model.request.LoginRequest;
import com.example.help_me_chat.model.response.ChangePwdResponse;
import com.example.help_me_chat.model.response.UserResponse;
import com.example.help_me_chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 用户相关接口（对齐前端ApiService的user接口）
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 登录
    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest request) {
        UserResponse response = new UserResponse();
        User user = userService.login(request.getUsername(), request.getPassword());
        if (user != null) {
            // 生成临时Token（后续替换为JWT）
            user.setToken(UUID.randomUUID().toString());
            userService.updateById(user);

            response.setCode(200);
            response.setMessage("登录成功");
            response.setData(user);
        } else {
            response.setCode(500);
            response.setMessage("用户名或密码错误");
        }
        return response;
    }

    // 注册
    @PostMapping("/register")
    public UserResponse register(@RequestBody LoginRequest request) {
        UserResponse response = new UserResponse();
        boolean success = userService.register(request.getUsername(), request.getPassword());
        if (success) {
            response.setCode(200);
            response.setMessage("注册成功");
        } else {
            response.setCode(500);
            response.setMessage("用户名已存在");
        }
        return response;
    }

    // 获取用户信息
    @GetMapping("/info")
    public BaseResponse<User> getUserInfo(@RequestHeader("Authorization") String token) {
        // 简化：根据Token查询用户（后续替换为JWT解析）
        // 临时实现：根据用户名查询（实际应解析Token中的userId）
        User user = userService.list().get(0); // 临时取第一个用户
        return BaseResponse.success(user);
    }

    // 更新用户信息
    @PutMapping("/info")
    public BaseResponse<Void> updateUserInfo(@RequestHeader("Authorization") String token, @RequestBody User user) {
        boolean success = userService.updateUserInfo(user);
        if (success) {
            return BaseResponse.success();
        } else {
            return BaseResponse.error("更新失败");
        }
    }

    // 修改密码
    @PostMapping("/changePwd")
    public ChangePwdResponse changePassword(@RequestHeader("Authorization") String token, @RequestBody ChangePwdRequest request) {
        ChangePwdResponse response = new ChangePwdResponse();
        boolean success = userService.changePassword(request);
        if (success) {
            response.setMessage("密码修改成功");
        } else {
            response.setMessage("旧密码错误或用户不存在");
        }
        return response;
    }

    // 搜索用户
    @GetMapping("/search")
    public BaseResponse<User> searchUser(@RequestHeader("Authorization") String token, @RequestParam("keyword") String keyword) {
        User user = userService.searchUser(keyword);
        return BaseResponse.success(user);
    }

    // 退出登录
    @PostMapping("/logout")
    public BaseResponse<Void> logout(@RequestHeader("Authorization") String token) {
        // 简化：清空Token
        User user = userService.list().get(0);
        user.setToken("");
        userService.updateById(user);
        return BaseResponse.success();
    }
}