package com.example.circle.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @date 2021/2/27 16:04
 */
@Component
public interface CircleGiveMapper {
    /**
     * 圈子点赞表
     * 查看帖子点过赞的头像
     * @param zqId
     * @return
     */
    @Select("select b.avatar as giveAvatar from tb_circles_give a INNER JOIN tb_user b on a.u_id=b.id where zq_id=${zqId}")
    String[] selectCirclesGivePersonAvatar(@Param("zqId") int zqId);

    /**
     * 查询我是否点赞过这帖子
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*)) from tb_circles_give where u_id=${userId} and zq_id=${tid}")
    Integer whetherGive(@Param("userId") int userId,@Param("tid") int tid);

    /**
     * 得到帖子点赞数量
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*)) from tb_circles_give where zq_id=${tid}")
    Integer selectGiveNumber(@Param("tid") int tid);
}
