package com.example.gold.service;

import com.example.common.utils.ResultUtil;
import com.example.gold.entity.Gold;
import com.example.gold.entity.PostExceptional;

import java.util.List;

/**
 * @author MQ
 * @date 2021/4/13 14:31
 */
public interface IGoldService {

    /**
     * 查询金币
     * @return
     */
    List<Gold> queryGold();

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
}
