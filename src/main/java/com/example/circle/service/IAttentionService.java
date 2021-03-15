package com.example.circle.service;

import com.example.circle.entity.Attention;
import com.example.circle.vo.CircleClassificationVo;
import com.example.common.utils.Paging;
import com.example.user.entity.User;

import java.util.List;

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
    List<User> queryAttentionPerson(int userId);

    /**
     * 根据自己选择的标签筛选出类似的用户
     * @param userId
     * @return
     */
    List<User> queryRecommendedUserData(int userId);

    /**
     * 查询我关注人的发的帖子
     * @param userId
     * @return
     */
    List<CircleClassificationVo> queryPostsPeopleFollow(int userId, Paging paging);
}
