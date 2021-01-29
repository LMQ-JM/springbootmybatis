package com.example.home.service;

import com.example.circle.entity.Circle;
import com.example.common.utils.Paging;
import com.example.home.entity.Resources;
import com.example.tags.entity.Tag;

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
     * 根据一级标签id查询二级标签
     * @param id 一级标签id
     * @return
     */
    List<Tag> selectSecondaryLabel(int id);

    /**
     * 根据二级级标签id查询帖子 如果还有第三级标签 先查询出第三级标签
     * @param id 二级级标签id
     * @param paging 分页
     * @return
     */
    <T>T selectPostingOrLabel(int id,Paging paging);

    /**
     * 根据三级标签id查询帖子
     * @param id 三级标签id
     * @param paging 分页
     * @return
     */
    List<Resources> selectPostingByThreeLabelId(int id, Paging paging);

    /**
     * 查询单个资源帖子
     * @param id 单个资源帖子id
     * @return
     */
    Resources selectSingleResourcePost(int id);

}
