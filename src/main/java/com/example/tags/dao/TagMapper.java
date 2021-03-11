package com.example.tags.dao;

import com.example.tags.entity.Tag;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/1/21 16:00
 */
@Component
public interface TagMapper {



    /**
     *  查询所有圈子中的一级标签
     * @param type
     * @return
     */
    @Select("select * from tb_tag where is_delete=1 and type=${type}")
    List<Tag> selectResourcesAllTag(@Param("type") int type);



    /**
     * 根据一级标签id查询二级标签
     * @param tid 一级标签id
     * @return
     */
    @Select("select * from tb_tags where t_id=${tid} and is_delete=1")
    List<Tag> selectResourcesAllTags(@Param("tid") int tid);


    /**
     * 查询资源的第一级标签
     * @param type
     * @return
     */
    @Select("select * from tb_tag where type=${type} and is_delete=1")
    List<Tag> selectFirstLevelLabelResource(@Param("type") int type);

    /**
     * 根据二级标签id查询一级标签
     * @param id 二级标签id
     * @return
     */
    @Select("select t_id from tb_tags where id=${id} and is_delete=1")
    int queryLabelAccordingSecondaryId(@Param("id") int id);

}
