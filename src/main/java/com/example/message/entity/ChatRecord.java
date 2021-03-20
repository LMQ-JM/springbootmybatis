package com.example.message.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/20 10:23
 * 聊天记录实体类
 */
@Data
public class ChatRecord {

    /**
     * 主键
     */
    private int id;

    /**
     * 发送人id
     */
    private int fUserId;

    /**
     * 接受人id
     */
    private int jUserId;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 消息类型
     */
    private int messageType;

    /**
     * 发送时间
     */
    private String createAt;

    /**
     * (0未读，1已读)
     */
    private int readUnread;

    /**
     * 用户消息标识
     */
    private long mCode;
}
