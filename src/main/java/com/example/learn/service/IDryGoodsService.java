package com.example.learn.service;

import com.example.common.utils.Paging;
import com.example.learn.Vo.DryGoodsTagVo;
import com.example.learn.entity.DryGoods;

/**
 * @author JC
 * @date 2021/4/16 15:56
 */
public interface IDryGoodsService {

    /**
     * 根据状态查询学习信息
     * @param type 0:提问； 1:干货; 2:公开课
     * @param paging 分页
     * @return
     */
    Object queryLearnList(int type, Paging paging);

    /**
     * 根据id查询干货详情
     * @param id 干货id
     * @param userId 用户id
     * @return
     */
    DryGoodsTagVo queryDryGoodsById(int id,int userId);

    /**
     * 发布干货帖
     * @param dryGoods
     * @return
     */
    int addDryGoods(DryGoods dryGoods);

    /**
     * 干货帖点赞
     * @param id 干货帖子id
     * @param userId 点赞人id
     * @return
     */
    int giveLike(int id, int userId);

    /**
     * 干货帖收藏
     * @param id 干货帖子id
     * @param userId 收藏人id
     * @return
     */
    int giveCollect(int id, int userId);
}
