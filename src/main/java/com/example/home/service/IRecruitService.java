package com.example.home.service;

import com.example.common.utils.Paging;
import com.example.home.entity.JobWanted;
import com.example.home.vo.RecruitVo;

import java.util.List;

/**
 * @author MQ
 * @create 2021/2/20
 **/
public interface IRecruitService {

    /**
     * 根据条件查询人才里面的招牌信息
     * @param typeId 类型id
     * @param paging 分页
     * @param userId 用户id
     * @param orderBy 0最热 1最新
     * @return
     */
    List<RecruitVo> selectSignboardInformation(int typeId, Paging paging,int userId,int orderBy);

    /**
     * 增加浏览量
     * @param id 招聘信息逐渐id
     * @return
     */
    int increasePageViews(int id);

    /**
     * 查看招聘信息详情
     * @param id 招聘信息逐渐id
     * @return
     */
    RecruitVo selectViewDetails (int id);

    /**
     * 根据用户id查询已发布的照片信息
     * @param userId
     * @param paging
     * @return
     */
    List<RecruitVo> selectPostingBasedUserID (int userId, Paging paging);

    /**
     * 添加求职期望
     * @param jobWanted
     * @return
     */
    int addJobExpectations (JobWanted jobWanted);
}
