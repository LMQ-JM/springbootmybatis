package com.example.websocket.util;

import lombok.Data;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/16 10:24
 */
@Data
public class MsgInfo {

    /**
     * 消息内容
     */
    private String msg;



    /**
     * 发给谁
     */
    private String toId;

    /**
     * 1--连接请求
     * 2--心跳消息
     * 3--单聊消息
     * 4--群聊信息
     * 5--家族信息
     *
     * 6 --消息通知-->事件
     * 7 --装备--未做
     * 8 --跟班产出
     * 9 --金矿--未做
     * 10 --活动--未做
     * 11 --商店--未做
     * 12 --好友红包成熟通知--未做
     *
     *
     * 100--强制下线
     * 101-申请添加好友(未在线消息以单聊消息形式发送,则type==3)
     */
    private Integer type;

    /**
     * 谁发的信息
     */
    private String cid;

    /**
     * 群聊id
     */
    private Long manyChatId;

    /**
     * 1--文本
     * 2--图片
     * 3--音频
     */
    private Integer msgType;

    /**
     * 发送时间
     */
    private String sendDate;

    /**
     * 发送Id的集合
     */
    private List<Long> toIdList;

    /**
     * 用户与用户之间的唯一标识
     */
    private long uniqueIdentification;
}
