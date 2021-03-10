package com.example.home.dao;

import com.example.home.entity.CommunityUser;
import com.example.home.vo.CommunityVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

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
    @Select("select a.*,b.user_name,b.id as userId from tb_community a INNER JOIN tb_user b on a.user_id=b.id where a.tag_id=${id}")
    CommunityVo selectCommunityCategoryId(@Param("id") int id);

    /**
     * 根据圈子id查询该圈子有多少人
     * @param id 圈子id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_community_user where community_id=${id} ")
    int selectTotalNumberCirclesById(@Param("id") int id);


    /**
     * 根据圈子id查询这个圈子有哪些人参加了讨论
     * @param id 圈子id
     * @return
     */
    @Select("select c.avatar from tb_community a INNER JOIN tb_resources b on  a.tag_id=b.tags_two INNER JOIN tb_user c on b.u_id=c.id where a.id=${id} and b.is_delete=1 GROUP BY c.id")
    String[] selectCirclesAvatar(@Param("id") int id);

    /**
     * 根据圈子id查询这个圈子里面的人
     * @param id 圈子id
     * @return
     */
    @Select("select b.avatar from tb_community_user a INNER JOIN tb_user b on a.user_id=b.id where a.community_id=${id}")
    String[] selectCirclesAvatars(@Param("id") int id);

    /**
     * 根据圈子id查询该用户是否在这个圈子里面
     * @param id
     * @return
     */
    @Select("select * from tb_community_user where community_id=${id}")
    List<CommunityUser> queryCommunityById(@Param("id") int id);

    /**
     * 根据圈子id和用户id查询是否在这个圈子里面
     * @param id 圈子id
     * @param userId 用户id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_community_user where community_id=${id} and user_id=${userId}")
    int queryWhetherThereCircle(@Param("id") int id,int userId);

    /**
     * 退出圈子
     * @param id 圈子id
     * @param userId 用户id
     * @return
     */
    @Delete("delete from tb_community_user where community_id=${id} and user_id=${userId}")
    int exitGroupChat(@Param("id") int id,int userId);


}

