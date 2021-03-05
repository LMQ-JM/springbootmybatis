package com.example.circle.service.impl;

import com.example.circle.dao.CommentMapper;
import com.example.circle.dao.PostReplyMapper;
import com.example.circle.entity.Comment;
import com.example.circle.entity.PostReply;
import com.example.circle.service.ICommentService;
import com.example.circle.vo.CommentReplyVo;
import com.example.circle.vo.PostReplyVo;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.user.dao.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/4 14:51
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostReplyMapper postReplyMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public int addComment(Comment comment) {
        comment.setCreateAt(System.currentTimeMillis()/1000+"");
        comment.setGiveStatus(0);
        //添加评论
        int i = commentMapper.addComment(comment);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return i;
    }

    @Override
    public int addSecondLevelComment(PostReply postReply) {
        postReply.setCreateAt(System.currentTimeMillis()/1000+"");
        postReply.setReplyGiveStatus(0);

        //添加二级评论
        int i = postReplyMapper.addSecondLevelComment(postReply);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return i;
    }

    @Override
    public List<CommentReplyVo> queryComments(int tId) {
        //查询一级评论
        List<CommentReplyVo> commentReplyVos = commentMapper.queryComment(tId);
        //根据一级评论id查询二级评论
        for (CommentReplyVo s : commentReplyVos){

            List<PostReplyVo> postReplies = postReplyMapper.queryPostReplyComment(s.getId());
            s.setCommentSize(postReplies.size());
            for (PostReplyVo a :postReplies){
                String userName = userMapper.selectUserById(a.getBhId()).getUserName();
                if(userName==null){
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
                a.setUName(userName);
            }
            s.setPostReplyList(postReplies);
        }
        return commentReplyVos;
    }
}
