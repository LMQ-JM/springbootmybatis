package com.example.circle.dao;

import com.example.circle.vo.CommentUserVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/2/27 15:37
 */
@Component
public interface CommentMapper {

    /**
     * 查看帖子的评论数量
     * @param tid
     * @return
     */
    @Select("select COALESCE(count(*)) from tb_comment where t_id=${tid}")
    Integer selectCommentNumber(@Param("tid") int tid);

    /**
     * 查询表面显示的评论
     * @param tid
     * @return
     */
    @Select(" select a.*,b.id as userId,b.user_name from tb_comment a INNER JOIN tb_user b on a.p_id=b.id where a.t_id=${tid} order by a.create_at desc limit 3")
    List<CommentUserVo> selectComment(@Param("tid") int tid);
}
