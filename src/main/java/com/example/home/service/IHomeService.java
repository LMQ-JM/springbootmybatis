package com.example.home.service;

import com.example.circle.entity.Circle;
import com.example.common.utils.Paging;
import com.example.common.utils.ReturnVo;
import com.example.home.entity.Resources;
import com.example.home.vo.HomeClassificationVo;
import com.example.home.vo.ResourcesVo;
import com.example.tags.entity.Tag;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.List;

/**
 * @author Administrator
 */
public interface IHomeService {


    /**
     * 搜索数据接口
     * @param postingName
     * @param paging
     * @return
     */
    List<Circle> selectAllSearch(String postingName, Paging paging);

    /**
     *  查询资源的第一级标签
     * @return
     */
    List<Tag> selectFirstLevelLabelResource();

    /**
     * 资源市场和学习交流的接口
     * @param id 一级标签id
     * @param paging 分页
     * @return
     */
    Object selectResourceLearningExchange(int id, Paging paging);

    /**
     * 根据社区分类id查询帖子
     * @param id 社区分类id
     * @param paging 分页
     * @return
     */
    List<Resources> selectPostsByCommunityCategoryId(int id, Paging paging);


    /**
     * 查询单个资源帖子
     * @param id 帖子id
     * @param userId 用户id
     * @return
     */
    ResourcesVo selectSingleResourcePost(int id,int userId) throws ParseException;

    /**
     * 根据二级标签id查询推荐数据
     * @param id
     * @return
     */
    List<HomeClassificationVo> selectRecommendedSecondaryTagId(int id);

    /**
     * 插叙所有资源数据
     * @param circle
     * @param page
     * @param limit
     * @param startTime
     * @param endTime
     * @return
     */
    ReturnVo selectResourcesAllPosting(Circle circle, Integer page, Integer limit, String startTime, String endTime) throws Exception;

    /**
     * 后台
     * 批量删除
     * @param id
     * @return
     */
    Integer resourcesDeletes(Integer[] id);


    /**
     * 增加资源贴
     * @param resources
     * @return
     */
    int addResourcesPost(Resources resources);


}
