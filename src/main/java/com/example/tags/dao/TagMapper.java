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
     * 查询所有圈子中的一级标签
     * @return
     */
    @Select("select * from tb_tag where is_delete=1 and type=1")
    List<Tag> selectAllTag();

    /**
     * 查询所有圈子中的一级标签
     * @return
     */
    @Select("select * from tb_tag where is_delete=1 and type=0")
    List<Tag> selectResourcesAllTag();

    /**
     * 根据一级标签id查询二级标签
     * @param tid 一级标签id
     * @return
     */
    @Select("select * from tb_tags where t_id=${tid} and type=1 and is_delete=1")
    List<Tag> selectAllTags(@Param("tid") int tid);

    /**
     * 根据一级标签id查询二级标签
     * @param tid 一级标签id
     * @return
     */
    @Select("select * from tb_tags where t_id=${tid} and type=0 and is_delete=1")
    List<Tag> selectResourcesAllTags(@Param("tid") int tid);

}
