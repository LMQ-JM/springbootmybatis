package com.example.learn.dao;

import com.example.learn.vo.PublicClassTagVo;
import com.example.learn.vo.PublicClassVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/4 17:57
 */
@Component
public interface PublicClassMapper {

    /**
     * 查询公开课列表
     * @param sql
     * @return
     */
    @Select("select a.id,a.title,a.tags_two,a.cover_img,a.price,a.buyer_num,b.tag_name from " +
            "tb_public_class a LEFT JOIN tb_tags b on a.tags_two = b.id where a.is_delete = 1 ${sql}")
    List<PublicClassTagVo> queryPublicClassList(@Param("sql") String sql);

    /**
     * 根据id查询公开课详情
     * @param id
     * @return
     */
    @Select("select * from tb_public_class where id = ${id} and is_delete = 1")
    PublicClassVo queryPublicClassById(@Param("id") int id);

    /**
     * 修改公开课收藏数
     * @param id
     * @param math
     * @return
     */
    @Update("update tb_public_class set collect = collect ${math} 1 where id = ${id}")
    int updatePublicClassCollect(@Param("id") int id,@Param("math") String math);
}
