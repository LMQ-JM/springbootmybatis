package com.example.circle.controller;

import com.example.circle.entity.Comment;
import com.example.circle.entity.PostReply;
import com.example.circle.service.ICommentService;
import com.example.circle.vo.CommentReplyVo;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/4 14:50
 */
@Api(tags = "评论API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/CommentController")
public class CommentController {

    @Autowired
    private ICommentService iCommentService;

    /**
     *
     * 添加评论
     * @return
     */
    @ApiOperation(value = "添加评论",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addComment")
    public int addComment(Comment comment){
        if(comment.getPId()==0 || comment.getBId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCommentService.addComment(comment);
    }

    /**
     *
     * 添加二级评论
     * @return
     */
    @ApiOperation(value = "添加二级评论",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addSecondLevelComment")
    public int addSecondLevelComment(PostReply postReply){
        if(postReply.getBhId()==0 || postReply.getHId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCommentService.addSecondLevelComment(postReply);
    }

    /**
     *
     * 根据帖子id查询评论
     * @return
     */
    @ApiOperation(value = "根据帖子id查询评论",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryComments")
    public List<CommentReplyVo> queryComments(int tId){
        if(tId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCommentService.queryComments(tId);
    }
}
