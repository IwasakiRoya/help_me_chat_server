package com.example.help_me_chat.controller;

import com.example.help_me_chat.common.BaseResponse;
import com.example.help_me_chat.entity.Friend;
import com.example.help_me_chat.entity.FriendRequestEntity; // 改用重命名后的实体类
import com.example.help_me_chat.model.request.FriendRequest; // 前端请求模型（保留）
import com.example.help_me_chat.model.request.HandleFriendRequest;
import com.example.help_me_chat.service.FriendService;
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

    // 发送好友请求（参数用前端请求模型，无冲突）
    @PostMapping("/request")
    public BaseResponse<Void> sendFriendRequest(
            @RequestHeader("Authorization") String token,
            @RequestBody FriendRequest request) { // 前端请求模型
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
        // 临时：实际应解析Token中的userId（后续替换为JWT解析逻辑）
        String userId = "1000";
        // 返回值类型和Service一致（FriendRequestEntity）
        List<FriendRequestEntity> list = friendService.getFriendRequests(userId);
        return BaseResponse.success(list);
    }

    // 处理好友请求（无冲突）
    @PostMapping("/handle")
    public BaseResponse<Void> handleFriendRequest(
            @RequestHeader("Authorization") String token,
            @RequestBody HandleFriendRequest request) {
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
        // 临时：实际应解析Token中的userId
        String userId = "1000";
        List<Friend> list = friendService.getFriendList(userId);
        return BaseResponse.success(list);
    }
}