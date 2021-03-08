package com.example.circle.service.impl;

import com.example.circle.dao.CommentMapper;
import com.example.circle.dao.PostReplyMapper;
import com.example.circle.entity.Comment;
import com.example.circle.entity.CommentGive;
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
    public List<CommentReplyVo> queryComments(int tId,int userId) {
        //查询一级评论
        List<CommentReplyVo> commentReplyVos = commentMapper.queryComment(tId);
        //根据一级评论id查询二级评论
        for (CommentReplyVo s : commentReplyVos){
            //得到评论点赞数量
            int i = commentMapper.queryCommentGiveNum(s.getId(), 0);
            s.setCommentGiveNum(i);


            //查看一级评论是否点赞
            if(userId!=0){
                //是否点赞
                CommentGive commentGive = commentMapper.queryWhetherGives(userId, 0,s.getId());
                if(commentGive!=null){
                    s.setCommentGiveStatus(1);
                }
            }


            //根据一级评论id查询二级的评论
            List<PostReplyVo> postReplies = postReplyMapper.queryPostReplyComment(s.getId());

            //得到每个一级评论下面的二级评论数量
            s.setCommentSize(postReplies.size());

            for (PostReplyVo a :postReplies){
                //得到二级评论评论点赞数量
                int i1 = commentMapper.queryCommentGiveNum(a.getId(), 1);
                a.setTwoCommentGiveNum(i1);

                //查看二级评论是否点赞
                if(userId!=0){
                    //是否点赞
                    CommentGive commentGive = commentMapper.queryWhetherGives(userId, 1,a.getId());
                    if(commentGive!=null){
                        a.setTwoCommentGiveStatus(1);
                    }
                }

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

    @Override
    public int addCommentGive(CommentGive commentGive) {

        CommentGive commentGive1=null;

        int i=0;

        commentGive.setCreateAt(System.currentTimeMillis()/1000+"");
        //添加一级评论点赞信息
        if(commentGive.getType()==0){
            commentGive.setType(0);
            commentGive1= commentMapper.queryWhetherGive(commentGive.getDId(), 0, commentGive.getCommentId());

            if(commentGive1!=null){
                //如果等于1就是点赞的状态 在进去就是取消点赞 状态改为0
                if(commentGive1.getGiveStatus()==1){
                    i = commentMapper.updateCommentGiveStatus(0, commentGive.getDId(), commentGive.getCommentId(), 0);
                    if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }
                }

                if(commentGive1.getGiveStatus()==0){
                     i = commentMapper.updateCommentGiveStatus(1, commentGive.getDId(), commentGive.getCommentId(), 0);
                     if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                     }
                }
            }else{
                 i = commentMapper.addCommentGive(commentGive);
                if(i<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"评论失败");
                }
            }

        }

        //添加二级评论信息
        if(commentGive.getType()==1){
             commentGive.setType(1);

             commentGive1= commentMapper.queryWhetherGive(commentGive.getDId(), 1, commentGive.getCommentId());

            if(commentGive1!=null){
                //如果等于1就是点赞的状态 在进去就是取消点赞 状态改为0
                if(commentGive1.getGiveStatus()==1){
                    i = commentMapper.updateCommentGiveStatus(0, commentGive.getDId(), commentGive.getCommentId(), 1);
                    if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }
                }

                if(commentGive1.getGiveStatus()==0){
                     i = commentMapper.updateCommentGiveStatus(1, commentGive.getDId(), commentGive.getCommentId(), 1);
                     if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                     }
                }
            }else{
                 i = commentMapper.addCommentGive(commentGive);
                if(i<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"评论失败");
                }
            }
        }


        return i;
    }
}
