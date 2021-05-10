package com.example.learn.service;

import com.example.learn.vo.PublicClassVo;

/**
 * @author JC
 * @date 2021/5/6 9:45
 */
public interface IPublicClassService {

    /**
     * 根据id查询公开课详情
     * @param id 公开课id
     * @param userId 用户id
     * @return
     */
    PublicClassVo queryPublicClassById(int id,int userId);

    /**
     * 公开课收藏
     * @param id
     * @param userId
     * @return
     */
    int giveCollect(int id,int userId);
}
