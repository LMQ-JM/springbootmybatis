package com.example.learn.dao;

import com.example.learn.vo.DryGoodsTagVo;
import com.example.learn.vo.DryGoodsVo;
import com.example.learn.entity.DryGoods;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JC
 * @date 2021/4/16 16:03
 */
@Component
public interface DryGoodsMapper {

    /**
     * 查询干货列表
     * @param sql
     * @return
     */
    @Select("select id,title,description,cover_img from tb_dry_goods where is_delete = 1 ${sql}")
    List<DryGoodsVo> queryDryGoodsList(@Param("sql") String sql);

    /**
     * 根据id查询干货详情
     * @param id
     * @return
     */
    @Select("select a.*,b.tag_name,COALESCE(SUM(c.gold_num),0) as goldNum from tb_dry_goods a INNER JOIN tb_tags b " +
            "on a.tags_two = b.id LEFT JOIN tb_learn_post_exceptional c on a.id = c.t_id " +
            "where a.id = ${id} and a.is_delete = 1")
    DryGoodsTagVo queryDryGoodsById(@Param("id") int id);

    /**
     * 增加干货帖
     * @param dryGoods
     * @return
     */
    @Insert("insert into tb_dry_goods(u_id,title,tags_one,tags_two,description,cover_img,content,create_at) value(${dryGoods.uId},#{dryGoods.title},${dryGoods.tagsOne},${dryGoods.tagsTwo},#{dryGoods.description},#{dryGoods.coverImg},#{dryGoods.content},#{dryGoods.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="dryGoods.id",keyColumn="id")
    int addDryGoods(@Param("dryGoods") DryGoods dryGoods);

    /**
     * 修改帖子点赞数
     * @param id
     * @param math
     * @return
     */
    @Update("update tb_dry_goods set favour = favour ${math} 1 where id = ${id}")
    int updateDryGoodsGive(@Param("id") int id,@Param("math") String math);

    /**
     * 修改帖子收藏数
     * @param id
     * @param math
     * @return
     */
    @Update("update tb_dry_goods set collect = collect ${math} 1 where id = ${id}")
    int updateDryGoodsCollect(@Param("id") int id,@Param("math") String math);
}
