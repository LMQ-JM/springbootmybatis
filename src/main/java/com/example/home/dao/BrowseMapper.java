package com.example.home.dao;

import com.example.home.entity.Browse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @create 2021/2/22
 **/
@Component
public interface BrowseMapper {
    /**
     * 增加浏览量
     * @param browse
     * @return
     */
    @Insert("insert into tb_browse(u_id,zq_id,type,create_at)values(${browse.uId},${browse.zqId},${browse.type},${browse.createAt})")
    int addBrowse(@Param("browse") Browse browse);

    /**
     * 查询上次观看帖子的时间
     * @param tid
     * @param userId
     * @return
     */
    @Select("select create_at from tb_browse where zq_id=${tid} and u_id=${userId} and type=0 order by create_at desc limit 1")
    String selectCreateAt(@Param("tid") int tid,@Param("userId") int userId);

    /**
     * 统计帖子的浏览量
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_browse where zq_id=${tid} and type=0")
    int countPostNum(@Param("tid")  int tid);


    /**
     * 根据帖子查询观看过人的头像
     * @param tId
     * @return
     */
    @Select("select b.avatar from tb_browse a INNER JOIN tb_user b on a.u_id=b.id where a.zq_id=${tId} and a.type=0 GROUP BY b.id limit 6")
    String[] selectBrowseAvatar(@Param("tId") int tId);
}
