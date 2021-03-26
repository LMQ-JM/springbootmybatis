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
     * 根据传的值查询对应的一级标签
     * @param type 0资源 1圈子
     * @return
     */
    @Select("select * from tb_tag where is_delete=1 and type=${type}")
    List<Tag> selectResourcesAllTag(@Param("type") int type);

    /**
     *  根据传的值查询对应的二级标签
     * @param type 0资源 1圈子
     * @return
     */
    @Select("select * from tb_tags where is_delete=1 and type=${type}")
    List<Tag> queryCorrespondingSecondaryLabel(@Param("type") int type);

    /**
     * 根据一级标签id查询二级标签
     * @param tid 一级标签id
     * @return
     */
    @Select("select * from tb_tags where t_id=${tid} and is_delete=1")
    List<Tag> selectResourcesAllTags(@Param("tid") int tid);



    /**
     * 根据二级标签id查询一级标签
     * @param id 二级标签id
     * @return
     */
    @Select("select t_id from tb_tags where id=${id} and is_delete=1")
    int queryLabelAccordingSecondaryId(@Param("id") int id);


    /**
     * 根据二级标签组查询每个标签发过多少个帖子
     *
     * @param id   标签id组
     * @param tableName 表名
     * @return
     */
    /*@Select({"<script>" +
            "select count(*) as num,b.id,b.tag_name,b.img_url from ${tableName} a right JOIN tb_tags b on a.tags_two=b.id " +
            " WHERE a.tags_two in " +
            "<foreach item = 'item' index = 'index' collection = 'list' open='(' separator=',' close=')'>" +
            "${item}" +
            "</foreach> GROUP BY a.tags_two" +
            "</script>"})*/
    @Select("select COALESCE(count(a.tags_two),0) from ${tableName} a  where a.tags_two=${id} and a.is_delete=1")
    int selectTagsNum(@Param("id") int id, @Param("tableName") String tableName);

}
