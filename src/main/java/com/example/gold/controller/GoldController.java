package com.example.gold.controller;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ResultUtil;
import com.example.gold.entity.PostExceptional;
import com.example.gold.service.IGoldService;
import com.example.gold.vo.UserGoldCoinsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * @author MQ
 * @date 2021/4/13 14:24
 */
@Api(tags = "金币API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/GoldController")
public class GoldController {

    @Autowired
    private IGoldService iGoldService;


    /**
     * 打赏帖子
     * @return
     */
    @ApiOperation(value = "打赏帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/postExceptional")
    public ResultUtil postExceptional(int rewardedUserId,PostExceptional postExceptional){
        if(rewardedUserId==0 || postExceptional.getUId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        ResultUtil resultUtil = iGoldService.postExceptional(rewardedUserId, postExceptional);
        return resultUtil;
    }

    /**
     * 签到
     * @return
     */
    @ApiOperation(value = "签到",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/signIn")
    public void signIn(int userId,int goldNumber){
         iGoldService.signIn(userId,goldNumber);
    }

    /**
     * 查询签到的数据
     * @return
     */
    @ApiOperation(value = "查询签到的数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryCheckedInData")
    public UserGoldCoinsVo queryCheckedInData(Integer userId) throws ParseException {
        return iGoldService.queryCheckedInData(userId);
    }

}
