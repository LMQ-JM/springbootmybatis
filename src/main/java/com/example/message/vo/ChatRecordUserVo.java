package com.example.message.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/21 11:58
 */
@Data
public class ChatRecordUserVo {

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

    /**
     * 双方用户信息
     */
    private UsersVo usersVo;
}
