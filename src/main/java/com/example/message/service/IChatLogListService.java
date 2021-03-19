package com.example.message.service;

import com.example.message.entity.ChatLogList;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/19 17:17
 */
public interface IChatLogListService {

    /**
     * 添加聊天列表
     * @param chatLogList 聊天列表对象
     * @return
     */
    int addChatList( ChatLogList chatLogList);

    /**
     * 查询聊天列表
     * @param userId 当前登录人id
     * @return
     */
    List<ChatLogList> queryChatList(int userId);
}
