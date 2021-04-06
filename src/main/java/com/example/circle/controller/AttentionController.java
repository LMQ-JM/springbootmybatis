package com.example.circle.controller;

import com.example.circle.entity.Attention;
import com.example.circle.service.IAttentionService;
import com.example.circle.vo.CircleClassificationVo;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.Paging;
import com.example.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/6 13:21
 */
@Api(tags = "关注API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/AttentionController")
public class AttentionController {

    @Autowired
    private IAttentionService iAttentionService;

    /**
     *
     * 添加关注信息
     * @return
     */
    @ApiOperation(value = "添加关注信息",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addAttention")
    public int addAttention(Attention attention){
        if(attention.getGuId()==0 || attention.getBgId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iAttentionService.addAttention(attention);
    }


    /**
     *
     * 查询我关注的人
     * @return
     */
    @ApiOperation(value = "查询我关注的人",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryAttentionPerson")
    public List<User> queryAttentionPerson(int userId){
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iAttentionService.queryAttentionPerson(userId);
    }



    /**
     *
     * 查询我关注的人发的帖子
     * @return
     */
    @ApiOperation(value = "查询我关注的人发的帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryPostsPeopleFollow")
    public List<CircleClassificationVo> queryPostsPeopleFollow(int userId, Paging paging){
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iAttentionService.queryPostsPeopleFollow(userId,paging);
    }

    /**
     *
     * 查询你可能感兴趣的人
     * @return
     */
    @ApiOperation(value = "查询你可能感兴趣的人",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryRecommendedUserData")
    public List<User> queryRecommendedUserData(int userId){
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iAttentionService.queryRecommendedUserData(userId);
    }



}
