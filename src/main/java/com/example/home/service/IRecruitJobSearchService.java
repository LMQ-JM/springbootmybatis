package com.example.home.service;

import com.example.common.utils.Paging;
import com.example.home.entity.RecruitJobSearch;
import com.example.home.vo.RecruitJobSearchVo;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/3/30 16:07
 */
public interface IRecruitJobSearchService {

    /**
     *根据条件查询人才里面的找工作信息
     * @param typeId 类型id 0找全职，1找兼职
     * @param paging 分页
     * @param orderBy 0最热 1最新
     * @return
     */
    List<RecruitJobSearchVo> queryJobInformation(int typeId, Paging paging, int orderBy);

    /**
     * 添加标签数据
     * @param recruitJobSearch
     * @param label 标签数组
     */
    void addJobHunting(RecruitJobSearch recruitJobSearch,Integer[] label) throws ParseException;

    /**
     *查看找工作信息详情
     * @param id
     * @return
     */
    RecruitJobSearchVo queryJobSearchDetails (int id);
}
