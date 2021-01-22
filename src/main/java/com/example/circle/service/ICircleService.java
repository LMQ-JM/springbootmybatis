package com.example.circle.service;

import com.example.circle.entity.Circle;
import com.example.common.utils.ReturnVo;

import java.text.ParseException;

/**
 * @author MQ
 * @date 2021/1/19 16:10
 */
public interface ICircleService {
    /**
     * 查询所有圈子的数据
     * @return
     */
    ReturnVo queryAllCircles();

    /**
     * 添加圈子
     * @param circle
     * @return
     */
    int addCirclePost(Circle circle);

    /**
     * 查询所有圈子的和资源数据
     * @param circle
     * @param page
     * @param limit
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    ReturnVo selectAllPosting(Circle circle,Integer page,Integer limit,String startTime,String endTime) throws ParseException;

    /**
     * 批量删除帖子
     * @param id
     * @return
     */
    Integer deletes(Integer[] id);
}
