package com.example.posting.dao;

import com.example.posting.vo.PostingReturnVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/1/15 10:08
 */
@Component
public interface PostingMapper {
    /**
     * 根据条件查询帖子
     * @param sql
     * @param paging
     * @return
     */
    @Select("select a.user_id as userId,a.id,a.name,b.name as username,b.avatar,a.likes,a.videos,a.create_at as createAt,a.end_at as endAt,a.address,a.cover,a.description,a.status from posting a LEFT JOIN user b on a.user_id=b.id where a.is_delete=0 ${sql} ORDER BY a.create_at DESC ${paging}")
    List<PostingReturnVo> selectAllPosting(@Param("sql") String sql,@Param("paging") String paging);


    /**
     * 统计帖子
     * @param sql
     * @return
     */
    @Select("select COALESCE(count(*),0) from posting a LEFT JOIN  user b on a.user_id=b.id where a.is_delete=0 ${sql}")
    Integer selectAllPostingCount(@Param("sql") String sql);

    /**
     * 批量删除
     * @param id
     * @return
     */
    @Update("update posting set is_delete=1  where id = ${id}")
    Integer deletes(@Param("id") int id);
}
