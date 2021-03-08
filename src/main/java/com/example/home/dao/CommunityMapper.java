package com.example.home.dao;

import com.example.home.vo.CommunityVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @date 2021/3/3 13:56
 */
@Component
public interface CommunityMapper {

    /**
     * 根据圈子id查询圈子信息
     * @param id 标签id
     * @return
     */
    @Select("select a.*,b.user_name,b.id as userId from tb_community a INNER JOIN tb_user b on a.user_id=b.id where a.tag_id=${id} and a.type=0")
    CommunityVo selectCommunityCategoryId(@Param("id") int id);

    /**
     * 根据圈子id查询该圈子有多少人
     * @param id 圈子id
     * @param type 类型 0资源，1圈子
     * @return
     */
    @Select("select count(*) from tb_community_user where community_id=${id} and type=${type}")
    int selectTotalNumberCirclesById(@Param("id") int id,@Param("type") int type);


    /**
     * 根据圈子id查询该圈子有多少人
     * @param id 圈子id
     * @param type 类型 0资源，1圈子
     * @return
     */
    @Select("select b.avatar from  tb_community_user a INNER JOIN tb_user b on a.user_id=b.id where a.community_id=${id} and a.type=${type}")
    String[] selectCirclesAvatar(@Param("id") int id,@Param("type") int type);
}

