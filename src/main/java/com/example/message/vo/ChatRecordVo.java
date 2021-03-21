package com.example.message.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/21 16:14
 */
@Data
public class ChatRecordVo {

    /**
     * 消息
     */
    private String message;

    /**
     * 消息类型
     */
    private int messageType;
}
