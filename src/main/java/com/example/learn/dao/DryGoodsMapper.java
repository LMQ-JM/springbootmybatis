package com.example.learn.dao;

import com.example.learn.Vo.DryGoodsVo;
import com.example.learn.entity.DryGoods;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
     * 增加干货帖
     * @param dryGoods
     * @return
     */
    @Insert("insert into tb_dry_goods(u_id,title,tags_one,tags_two,description,cover_img,content,create_at) value(${dryGoods.uId},#{dryGoods.title},${dryGoods.tagsOne},${dryGoods.tagsTwo},#{dryGoods.description},#{dryGoods.coverImg},#{dryGoods.content},#{dryGoods.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="dryGoods.id",keyColumn="id")
    int addDryGoods(@Param("dryGoods") DryGoods dryGoods);
}
