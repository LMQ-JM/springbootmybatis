package com.example.inform.dao;

import com.example.inform.vo.InformUserVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/22 10:39
 */
@Component
public interface InformMapper {

    /**
     * 查询评论通知
     * @param userId 当前用户id
     * @param type 1评论，2获赞
     * @param paging 分页
     * @return
     */
    @Select("select a.id,a.content,a.create_at,a.t_id,b.id as userId,b.user_name,b.avatar,c.cover " +
            "from tb_inform a INNER JOIN tb_circles c on a.t_id=c.id INNER JOIN tb_user b on a.notifier_id=b.id " +
            "where a.is_delete=1 and a.type=${type} and a.notified_party_id=${userId} and c.is_delete=1 order by a.create_at desc")
    List<InformUserVo> queryCommentsNotice(@Param("userId") int userId,@Param("type") int type, @Param("paging") String paging);



}
