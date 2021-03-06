package com.example.circle.dao;

import com.example.circle.entity.Give;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
    @Select("select b.avatar as giveAvatar from tb_circles_give a INNER JOIN tb_user b on a.u_id=b.id where a.zq_id=${zqId} and a.give_cancel=1 order by a.create_at limit 8")
    String[] selectCirclesGivePersonAvatar(@Param("zqId") int zqId);

    /**
     * 查询我是否点赞过这帖子
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*)) from tb_circles_give where u_id=${userId} and zq_id=${tid} and give_cancel=1")
    Integer whetherGive(@Param("userId") int userId,@Param("tid") int tid);

    /**
     * 得到帖子点赞数量
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*)) from tb_circles_give where zq_id=${tid} and give_cancel=1")
    Integer selectGiveNumber(@Param("tid") int tid);

    /**
     **增加点赞信息
     *@param id 帖子id
     *@param userId 用户id
     *@param createAt 创建时间
     * @return
     */
    @Insert("insert into tb_circles_give(zq_id,u_id,create_at,give_cancel)values(${id},${userId},#{createAt},1)")
    int givePost(@Param("id")int id,@Param("userId") int userId,@Param("createAt") String createAt);

    /**
     * 查询数据库是否存在该条数据
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    @Select("select * from tb_circles_give where u_id=${userId} and zq_id=${tid}")
    Give selectCountWhether(@Param("userId") int userId, @Param("tid") int tid);

    /**
     * 修改点赞状态
     * @param id 点赞id
     * @param status 状态id
     * @return
     */
    @Update("update tb_circles_give set give_cancel=${status} where id=${id}")
    int updateGiveStatus(@Param("id") int id,@Param("status") int status);
}
