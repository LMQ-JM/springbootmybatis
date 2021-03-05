package com.example.circle.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/5 10:12
 */
@Data
public class PostReplyVo {

    /**
     * 回复人id
     */
    private int userId;

    /**
     * 回复人名称
     */
    private String userName;

    /**
     * 回复人头像
     */
    private String avatar;

    /**
     *评论内容
     */
    private String hContent;

    /**
     * 评论点赞状态（0没有，1点赞）
     */
    private int replyGiveStatus;

    /**
     * 被回复人id
     */
    private int bhId;

    /**
     * 被回复人名称
     */
    private String uName;
}
