package com.example.circle.service;

import com.example.circle.entity.Attention;
import com.example.user.entity.User;

/**
 * @author MQ
 * @date 2021/3/6 13:26
 */
public interface IAttentionService {

    /**
     * 添加关注信息
     * @param attention
     * @return
     */
    int addAttention(Attention attention);

    /**
     * 查询我关注的人
     * @param userId 当前登录用户id
     * @return
     */
    User queryPeopleFollow(int userId);

    /**
     * 根据自己选择的标签筛选出类似的用户
     * @param userId
     * @return
     */
    User queryRecommendedUserData(int userId);
}
