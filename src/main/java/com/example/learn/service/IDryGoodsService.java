package com.example.learn.service;

import com.example.common.utils.Paging;
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
     * 发布干货帖
     * @param dryGoods
     * @return
     */
    int addDryGoods(DryGoods dryGoods);
}
