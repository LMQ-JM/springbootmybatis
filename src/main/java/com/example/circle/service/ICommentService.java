package com.example.circle.service;

import com.example.circle.entity.Comment;
import com.example.circle.entity.PostReply;
import com.example.circle.vo.CommentReplyVo;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/4 14:50
 */
public interface ICommentService {

    /**
     * 添加评论
     * @param comment
     * @return
     */
    int addComment(Comment comment);

    /**
     * 添加二级评论
     * @param postReply 二级评论对象
     * @return
     */
    int addSecondLevelComment(PostReply postReply);

    /**
     * 查询评论
     * @param tId 帖子id
     * @return
     */
    List<CommentReplyVo> queryComments(int tId);
}
