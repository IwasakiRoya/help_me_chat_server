package com.example.help_me_chat.controller;

import com.example.help_me_chat.common.BaseResponse;
import com.example.help_me_chat.entity.Friend;
import com.example.help_me_chat.entity.FriendRequestEntity; // 改用重命名后的实体类
import com.example.help_me_chat.entity.User;
import com.example.help_me_chat.model.request.FriendRequest; // 前端请求模型（保留）
import com.example.help_me_chat.model.request.HandleFriendRequest;
import com.example.help_me_chat.service.FriendService;
import com.example.help_me_chat.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 好友相关接口（对齐前端ApiService的friend接口）
 */
@RestController
@RequestMapping("/api/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private AuthUtil authUtil;

    // 发送好友请求（参数用前端请求模型，无冲突）
    @PostMapping("/request")
    public BaseResponse<Void> sendFriendRequest(
            @RequestHeader("Authorization") String token,
            @RequestBody FriendRequest request) { // 前端请求模型
        User user = authUtil.getUserByToken(token);
        if (user == null) {
            return BaseResponse.error("用户未登录或Token无效");
        }
        // 设置请求发送者ID
        request.setFromUserId(user.getUserId());
        boolean success = friendService.sendFriendRequest(request);
        if (success) {
            return BaseResponse.success();
        } else {
            return BaseResponse.error("发送请求失败");
        }
    }

    // 获取好友请求列表（返回值改用重命名后的实体类）
    @GetMapping("/requests")
    public BaseResponse<List<FriendRequestEntity>> getFriendRequests(
            @RequestHeader("Authorization") String token) {
        User user = authUtil.getUserByToken(token);
        if (user == null) {
            return BaseResponse.error("用户未登录或Token无效");
        }
        // 返回值类型和Service一致（FriendRequestEntity）
        List<FriendRequestEntity> list = friendService.getFriendRequests(user.getUserId());
        return BaseResponse.success(list);
    }

    // 处理好友请求（无冲突）
    @PostMapping("/handle")
    public BaseResponse<Void> handleFriendRequest(
            @RequestHeader("Authorization") String token,
            @RequestBody HandleFriendRequest request) {
        User user = authUtil.getUserByToken(token);
        if (user == null) {
            return BaseResponse.error("用户未登录或Token无效");
        }
        
        // 通过requestId获取请求信息，验证当前用户是否有权限处理
        FriendRequestEntity requestEntity = friendService.getFriendRequestById(request.getRequestId());
        if (requestEntity == null) {
            return BaseResponse.error("好友请求不存在");
        }
        
        // 验证当前用户是否是请求的接收者
        if (!requestEntity.getToUserId().equals(user.getUserId())) {
            return BaseResponse.error("无权限处理此好友请求");
        }
        
        // 调用服务处理好友请求
        boolean success = friendService.handleFriendRequest(request);
        if (success) {
            return BaseResponse.success();
        } else {
            return BaseResponse.error("处理请求失败");
        }
    }

    // 获取好友列表（无冲突）
    @GetMapping("/list")
    public BaseResponse<List<Friend>> getFriendList(
            @RequestHeader("Authorization") String token) {
        User user = authUtil.getUserByToken(token);
        if (user == null) {
            return BaseResponse.error("用户未登录或Token无效");
        }
        List<Friend> list = friendService.getFriendList(user.getUserId());
        return BaseResponse.success(list);
    }
}