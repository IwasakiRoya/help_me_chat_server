package com.example.help_me_chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.help_me_chat.entity.Friend;
import com.example.help_me_chat.entity.FriendRequestEntity; // 新类名
import com.example.help_me_chat.model.request.FriendRequest;
import com.example.help_me_chat.model.request.HandleFriendRequest;

import java.util.List;

public interface FriendService extends IService<Friend> {
    // 发送好友请求
    boolean sendFriendRequest(FriendRequest request);

    // 获取好友请求列表（返回新类名）
    List<FriendRequestEntity> getFriendRequests(String toUserId);

    // 处理好友请求
    boolean handleFriendRequest(HandleFriendRequest request);

    // 获取好友列表
    List<Friend> getFriendList(String myId);

    // 根据ID获取好友请求
    FriendRequestEntity getFriendRequestById(String requestId);
}