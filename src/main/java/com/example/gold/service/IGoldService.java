package com.example.gold.service;

import com.example.common.utils.ResultUtil;
import com.example.gold.entity.PostExceptional;
import com.example.gold.vo.UserGoldCoinsVo;

import java.text.ParseException;

/**
 * @author MQ
 * @date 2021/4/13 14:31
 */
public interface IGoldService {



    /**
     * 打赏帖子
     * @param rewardedUserId 被打赏人id
     * @param postExceptional
     * @return
     */
    ResultUtil postExceptional(int rewardedUserId, PostExceptional postExceptional);

    /**
     * 签到
     * @param userId 签到用户id
     * @param goldNumber 签到得到的金币数量
     */
    void signIn(int userId,int goldNumber);

    /**
     * 查询签到的数据
     * @param userId 用户id
     * @return
     */
    UserGoldCoinsVo queryCheckedInData(Integer userId) throws ParseException;
}
