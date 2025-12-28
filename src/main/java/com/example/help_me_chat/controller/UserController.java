package com.example.help_me_chat.controller;

import com.example.help_me_chat.common.BaseResponse;
import com.example.help_me_chat.entity.User;
import com.example.help_me_chat.model.request.ChangePwdRequest;
import com.example.help_me_chat.model.request.LoginRequest;
import com.example.help_me_chat.model.response.ChangePwdResponse;
import com.example.help_me_chat.model.response.UserResponse;
import com.example.help_me_chat.service.UserService;
import com.example.help_me_chat.util.AuthUtil;
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

    @Autowired
    private AuthUtil authUtil;

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
    public BaseResponse<User> getUserInfo(@RequestHeader("Authorization") String token, String userId) {
        User user = authUtil.getUserByToken(token);
        if (user == null) {
            return BaseResponse.error("用户未登录或Token无效");
        }
        if (userId.equals("useToken")) {
            return BaseResponse.success(user);
        }
        User byId = userService.getById(userId);
        if (byId != null) {
            return BaseResponse.success(byId);
        } else {
            return BaseResponse.error("用户不存在");
        }
    }

    // 更新用户信息
    @PutMapping("/info")
    public BaseResponse<Void> updateUserInfo(@RequestHeader("Authorization") String token, @RequestBody User updatedUser) {
        User user = authUtil.getUserByToken(token);
        if (user == null) {
            return BaseResponse.error("用户未登录或Token无效");
        }
        // 确保只能更新自己的信息
        updatedUser.setUserId(user.getUserId());
        boolean success = userService.updateUserInfo(updatedUser);
        if (success) {
            return BaseResponse.success();
        } else {
            return BaseResponse.error("更新失败");
        }
    }

    // 修改密码
    @PostMapping("/changePwd")
    public ChangePwdResponse changePassword(@RequestHeader("Authorization") String token, @RequestBody ChangePwdRequest request) {
        User user = authUtil.getUserByToken(token);
        if (user == null) {
            ChangePwdResponse response = new ChangePwdResponse();
            response.setCode(500);
            response.setMessage("用户未登录或Token无效");
            return response;
        }
        // 设置用户ID以确保修改正确的用户密码
        request.setUserId(user.getUserId());
        ChangePwdResponse response = new ChangePwdResponse();
        boolean success = userService.changePassword(request);
        if (success) {
            response.setCode(200);
            response.setMessage("密码修改成功");
        } else {
            response.setCode(500);
            response.setMessage("旧密码错误或用户不存在");
        }
        return response;
    }

    // 搜索用户
    @GetMapping("/search")
    public BaseResponse<User> searchUser(@RequestHeader("Authorization") String token, @RequestParam("keyword") String keyword) {
        User currentUser = authUtil.getUserByToken(token);
        if (currentUser == null) {
            return BaseResponse.error("用户未登录或Token无效");
        }
        User user = userService.searchUser(keyword);
        return BaseResponse.success(user);
    }

    // 退出登录
    @PostMapping("/logout")
    public BaseResponse<Void> logout(@RequestHeader("Authorization") String token) {
        User user = authUtil.getUserByToken(token);
        if (user == null) {
            return BaseResponse.error("用户未登录或Token无效");
        }
        // 清空Token
        user.setToken("");
        userService.updateById(user);
        return BaseResponse.success();
    }
}