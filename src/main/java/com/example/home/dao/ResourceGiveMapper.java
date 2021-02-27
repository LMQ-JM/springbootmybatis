package com.example.home.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @create 2021/2/22
 **/
@Component
public interface ResourceGiveMapper {

    /**
     * 资源点赞表
     * 查看帖子点过赞的头像
     * @param zqId
     * @return
     */
    @Select("select b.avatar as giveAvatar from tb_resources_give a INNER JOIN tb_user b on a.u_id=b.id where zq_id=${zqId}")
    String[] selectResourcesGivePersonAvatar(@Param("zqId") int zqId);




}
