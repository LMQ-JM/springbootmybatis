package com.example.circle.dao;

import com.example.circle.entity.Circle;
import com.example.circle.entity.Img;
import com.example.circle.vo.CircleLabelVo;
import com.example.home.entity.Resources;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/1/19 16:09
 */
@Component
public interface CircleMapper {

    /**
     * 查询所有圈子的数据
     * @return
     */
    @Select("SELECT a.id,a.content,b.name,a.img,a.type,a.video,a.favour,a.collect,a.browse FROM tb_circles a inner JOIN tb_tags b on a.tags_two=b.id")
    List<CircleLabelVo> queryAllCircles();

    /**
     * 统计圈子数据
     * @return
     */
    @Select("SELECT COALESCE(count(*),0) FROM tb_circles a inner JOIN tb_tags b on a.tags_two=b.id")
    Integer countAllCircles();

    /**
     * 增加圈子帖子
     * @param circle
     * @return
     */
    @Insert("insert into tb_circles(content,tags_one,tags_two,type,video,cover,create_at,u_id,user_name,avatar,title)values(#{circle.content},${circle.tagsOne},${circle.tagsTwo},${circle.type},#{circle.video},#{circle.cover},#{circle.createAt},${circle.uId},#{circle.userName},#{circle.avatar},#{circle.title})")
    @Options(useGeneratedKeys=true, keyProperty="circle.id",keyColumn="id")
    int addCirclePost(@Param("circle") Circle circle);

    /**
     * 添加图片
     * @param img
     * @return
     */
    @Insert("insert into tb_img(z_id,img_url,type,create_at)values(${img.zId},#{img.imgUrl},${img.type},#{img.createAt})")
    int addImg(@Param("img") Img img);

    /**
     * 根据条件查询所有圈子
     * @param sql 条件拼接
     * @param paging 分页拼接
     * @return
     */
    @Select("select a.id,a.content,b.tag_name,a.type,a.video,a.favour,a.collect,a.browse,a.title,a.avatar,a.create_at,a.user_name from tb_circles a INNER JOIN tb_tags b on a.tags_two=b.id where a.is_delete=1 ${sql} ORDER BY a.create_at desc ${paging}")
    List<CircleLabelVo> selectAllPosting(@Param("sql") String sql,@Param("paging") String paging);

    /**
     * 根据条件统计数量
     * @param sql
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_circles a INNER JOIN tb_tags b on a.tags_two=b.id where a.is_delete=1 ${sql}")
    Integer selectAllPostingCount(@Param("sql") String sql);

    /**
     * 批量删除
     * @param id
     * @return
     */
    @Update("update tb_circles set is_delete=0  where id = ${id}")
    Integer deletes(@Param("id") int id);

    /**
     * 根据圈子中的标签id查询帖子
     * @param id 二级标签id
     * @param paging 分页
     * @return
     */
    @Select("select a.* from tb_circles a INNER JOIN tb_tags b on a.tags_two=${id} ${paging}")
    List<Circle> selectPostsBasedTagIdCircle(@Param("id") int id, @Param("paging") String paging);
}
