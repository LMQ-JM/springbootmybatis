package com.example.learn.service.impl;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ConstantUtil;
import com.example.learn.dao.LearnCommentMapper;
import com.example.learn.dao.PostReplyLearnMapper;
import com.example.learn.entity.LearnComment;
import com.example.learn.entity.LearnCommentGive;
import com.example.learn.entity.LearnPostReply;
import com.example.learn.service.ICommentLearnService;
import com.example.learn.vo.LearnCommentReplyVo;
import com.example.learn.vo.LearnPostReplyVo;
import com.example.user.dao.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

/**
 * @author JC
 * @date 2021/4/21 9:42
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CommentLearnServiceImpl implements ICommentLearnService {

    @Autowired
    private LearnCommentMapper learnCommentMapper;

    @Autowired
    private PostReplyLearnMapper postReplyLearnMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public int addComment(LearnComment comment) throws ParseException {
        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(comment.getCommentContent(), token);
        if(identifyTextContent=="87014" || identifyTextContent.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        comment.setCreateAt(System.currentTimeMillis()/1000+"");
        comment.setGiveStatus(0);
        //添加评论
        int i = learnCommentMapper.addComment(comment);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return i;
    }

    @Override
    public int addSecondLevelComment(LearnPostReply postReply) throws ParseException {
        postReply.setCreateAt(System.currentTimeMillis()/1000+"");
        postReply.setReplyGiveStatus(0);

        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(postReply.getHContent(), token);
        if(identifyTextContent=="87014" || identifyTextContent.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        //添加二级评论
        int i = postReplyLearnMapper.addSecondLevelComment(postReply);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return i;
    }

    @Override
    public List<LearnCommentReplyVo> queryComments(int tId, int userId, int tType) {
        //查询一级评论
        List<LearnCommentReplyVo> commentReplyVos = learnCommentMapper.queryComment(tId,tType);
        //根据一级评论id查询二级评论
        for (LearnCommentReplyVo s : commentReplyVos){
            //得到评论点赞数量
            int i = learnCommentMapper.queryCommentGiveNum(s.getId(), 0);
            s.setCommentGiveNum(i);
            //查看一级评论是否点赞
            if(userId!=0){
                //是否点赞
                LearnCommentGive commentGive = learnCommentMapper.queryWhetherGives(userId, 0,s.getId());
                if(commentGive != null){
                    s.setCommentGiveStatus(1);
                }
            }
            //根据一级评论id查询二级的评论
            List<LearnPostReplyVo> postReplies = postReplyLearnMapper.queryPostReplyComment(s.getId());
            //得到每个一级评论下面的二级评论数量
            s.setCommentSize(postReplies.size());

            for (LearnPostReplyVo a :postReplies){
                //得到二级评论评论点赞数量
                int i1 = learnCommentMapper.queryCommentGiveNum(a.getId(), 1);
                a.setTwoCommentGiveNum(i1);
                //查看二级评论是否点赞
                if(userId!=0){
                    //是否点赞
                    LearnCommentGive commentGive = learnCommentMapper.queryWhetherGives(userId, 1,a.getId());
                    if(commentGive != null){
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
    public int addCommentGive(LearnCommentGive commentGive) {
        LearnCommentGive learnCommentGive = null;
        int i=0;
        commentGive.setCreateAt(System.currentTimeMillis()/1000+"");
        //添加一级评论点赞信息
        if(commentGive.getType()==0){
            commentGive.setType(0);
            learnCommentGive = learnCommentMapper.queryWhetherGive(commentGive.getDId(), 0, commentGive.getCommentId());

            if(learnCommentGive != null){
                //如果等于1就是点赞的状态 在进去就是取消点赞 状态改为0
                if(learnCommentGive.getGiveStatus() == 1){
                    i = learnCommentMapper.updateCommentGiveStatus(0, commentGive.getDId(), commentGive.getCommentId(), 0);
                    if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }
                }

                if(learnCommentGive.getGiveStatus()==0){
                    i = learnCommentMapper.updateCommentGiveStatus(1, commentGive.getDId(), commentGive.getCommentId(), 0);
                    if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }
                }
            }else{
                i = learnCommentMapper.addCommentGive(commentGive);
                if(i<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"一级评论点赞失败");
                }
            }

        }
        //添加二级评论点赞信息
        if(commentGive.getType()==1){
            commentGive.setType(1);

            learnCommentGive = learnCommentMapper.queryWhetherGive(commentGive.getDId(), 1, commentGive.getCommentId());

            if(learnCommentGive != null){
                //如果等于1就是点赞的状态 在进去就是取消点赞 状态改为0
                if(learnCommentGive.getGiveStatus() == 1){
                    i = learnCommentMapper.updateCommentGiveStatus(0, commentGive.getDId(), commentGive.getCommentId(), 1);
                    if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }
                }

                if(learnCommentGive.getGiveStatus() == 0){
                    i = learnCommentMapper.updateCommentGiveStatus(1, commentGive.getDId(), commentGive.getCommentId(), 1);
                    if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }
                }
            }else{
                i = learnCommentMapper.addCommentGive(commentGive);
                if(i<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"二级评论点赞失败");
                }
            }
        }

        return i;
    }
}
