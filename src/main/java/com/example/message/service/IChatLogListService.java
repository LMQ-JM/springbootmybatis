package com.example.message.service;

import com.example.message.entity.ChatLogList;
import com.example.message.entity.ChatRecord;
import com.example.message.vo.ChatRecordUserVo;

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
    String addChatList( ChatLogList chatLogList);

    /**
     * 查询聊天列表
     * @param userId 当前登录人id
     * @return
     */
    List<ChatLogList> queryChatList(int userId);

    /**
     * 根据用户唯一标识查询出用户与用户的聊天记录
     * @param uniqueIdentification 唯一标识
     * @return
     */
    List<ChatRecordUserVo> queryChattingRecords(long uniqueIdentification);

    /**
     * 单聊
     * @param chatRecord
     */
    void singleChat(ChatRecord chatRecord);
}
