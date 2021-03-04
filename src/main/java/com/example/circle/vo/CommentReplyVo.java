package com.example.circle.vo;

import com.example.circle.entity.PostReply;
import lombok.Data;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/4 15:13
 */
@Data
public class CommentReplyVo {

    private int id;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论点赞状态（0没有，1点赞）
     */
    private int giveStatus;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 二级评论集合
     */
    List<PostReply> postReplyList;
}
