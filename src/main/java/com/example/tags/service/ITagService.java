package com.example.tags.service;

import com.example.tags.entity.Tag;

import java.util.List;

/**
 * @author MQ
 * @date 2021/1/21 16:08
 */
public interface ITagService {


    /**
     * 查询所有圈子一级标签
     * @return
     */
    List<Tag> selectResourcesAllTag(int type);

    /**
     * 根据一级标签id查询二级标签
     * @param tid 一级标签id
     * @return
     */
    List<Tag> selectResourcesAllTags(int tid);


}
