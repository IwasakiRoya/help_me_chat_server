package com.example.help_me_chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.help_me_chat.entity.Friend;
import com.example.help_me_chat.entity.FriendRequestEntity; // 改为新类名
import com.example.help_me_chat.mapper.FriendMapper;
import com.example.help_me_chat.mapper.FriendRequestMapper;
import com.example.help_me_chat.model.request.FriendRequest; // 前端请求模型
import com.example.help_me_chat.model.request.HandleFriendRequest;
import com.example.help_me_chat.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {

    @Autowired
    private FriendRequestMapper friendRequestMapper;

    @Autowired
    private FriendMapper friendMapper;

    @Override
    public boolean sendFriendRequest(FriendRequest request) { // 前端请求模型（保留）
        // 检查是否已发送请求
        // 简化实现：直接创建请求
        FriendRequestEntity req = new FriendRequestEntity(); // 后端实体类（新类名）
        req.setRequestId(UUID.randomUUID().toString());
        req.setFromUserId(request.getFromUserId());
        req.setToUserId(request.getToUserId());
        req.setRequestMsg(request.getRequestMsg());
        req.setStatus(0); // 未处理
        return friendRequestMapper.insert(req) > 0;
    }

    @Override
    public List<FriendRequestEntity> getFriendRequests(String toUserId) { // 返回新类名
        return friendRequestMapper.selectByToUserId(toUserId);
    }

    @Override
    public boolean handleFriendRequest(HandleFriendRequest request) {
        // 查询请求（改用新类名）
        FriendRequestEntity req = friendRequestMapper.selectById(request.getRequestId());
        if (req == null) {
            return false;
        }
        // 更新请求状态
        req.setStatus(request.getStatus());
        friendRequestMapper.updateById(req);

        // 同意请求：创建好友关系
        if (request.getStatus() == 1) {
            Friend friend = new Friend();
            friend.setMyId(req.getToUserId());
            friend.setFriendId(req.getFromUserId());
            friendMapper.insert(friend);

            // 双向好友
            Friend friend2 = new Friend();
            friend2.setMyId(req.getFromUserId());
            friend2.setFriendId(req.getToUserId());
            friendMapper.insert(friend2);
        }
        return true;
    }

    @Override
    public List<Friend> getFriendList(String myId) {
        return friendMapper.selectByMyId(myId);
    }

    @Override
    public FriendRequestEntity getFriendRequestById(String requestId) {
        return friendRequestMapper.selectById(requestId);
    }
}