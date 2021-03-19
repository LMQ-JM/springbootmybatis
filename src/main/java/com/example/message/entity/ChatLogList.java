package com.example.message.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/19 16:40
 * 聊天列表用户信息
 */
@Data
public class ChatLogList {

    private int id;

    /**
     * 跟我聊天人id
     */
    private int userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 当前登录人用户id
     */
    private int dqUserId;

    /**
     * 最新一条聊天记录
     */
    private String newestChatLog;

    /**
     *创建时间
     */
    private String createAt;

    /**
     * 是有有效（1有效，0无效） 默认1
     */
    private int isDelete;
}
