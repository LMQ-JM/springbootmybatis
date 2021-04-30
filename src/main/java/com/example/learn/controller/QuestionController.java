package com.example.learn.controller;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.learn.entity.Question;
import com.example.learn.service.IQuestionService;
import com.example.learn.vo.QuestionTagVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * @author JC
 * @date 2021/4/29 11:45
 */
@Api(tags = "提问API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/QuestionController")
public class QuestionController {

    @Autowired
    private IQuestionService iQuestionService;

    /**
     * 发布提问帖
     * @param question
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "发布提问帖",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addQuestion")
    public int addQuestion(Question question) throws Exception{
        return iQuestionService.addQuestion(question);
    }

    /**
     * 根据id查询提问详情
     * @param id
     * @param userId
     * @return
     * @throws ParseException
     */
    @ApiOperation(value = "根据id查询干货详情",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryQuestionById")
    public QuestionTagVo queryQuestionById(int id,int userId) throws Exception{
        if(id==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iQuestionService.queryQuestionById(id,userId);
    }

    /**
     * 提问帖点赞
     * @param id 提问帖子id
     * @param userId 点赞人id
     * @return
     */
    @ApiOperation(value = "点赞",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/giveLike")
    public int giveLike(int id, int userId){
        if(id==0 || userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iQuestionService.giveLike(id,userId);
    }

    /**
     * 提问帖收藏
     * @param id 提问帖子id
     * @param userId 收藏人id
     * @return
     */
    @ApiOperation(value = "收藏",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/giveCollect")
    public int giveCollect(int id, int userId){
        if(id==0 || userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iQuestionService.giveCollect(id,userId);
    }
}
