package com.example.tags.service;

import com.example.home.entity.Community;
import com.example.tags.entity.Tag;
import com.example.tags.vo.AllTagVo;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/1/21 16:08
 */
public interface ITagService {


    /**
     * 查询所有圈子一级标签
     * @param type 0 资源 1圈子
     * @return
     */
    List<Tag> selectResourcesAllTag(int type);

    /**
     * 根据一级标签id查询二级标签
     * @param tid 一级标签id
     * @return
     */
    List<Tag> selectResourcesAllTags(int tid);

    /**
     *查询所有一级标签和二级标签
     * @return
     */
    List<AllTagVo> queryAllPrimaryAndSecondaryTags();

    /**
     * 添加二级标签
     * @param tag 标签对象
     * @param community 社区对象
     * @throws ParseException
     */
    void addTag(Tag tag, Community community) throws ParseException;

}
