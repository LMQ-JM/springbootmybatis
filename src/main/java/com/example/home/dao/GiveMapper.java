package com.example.home.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @create 2021/2/22
 **/
@Component
public interface GiveMapper {

    /**
     * 查看帖子点过赞的头像
     * @param zqId
     * @return
     */
    @Select("select b.avatar as giveAvatar from tb_give a INNER JOIN tb_user b on a.u_id=b.id where zq_id=${zqId}")
    String[] selectGivePersonAvatar(@Param("zqId") int zqId);

}
