package com.example.personalCenter.dao;

import com.example.circle.entity.Attention;
import com.example.home.vo.HomeClassificationVo;
import com.example.personalCenter.vo.UserMessageVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/9 9:34
 */
@Component
public interface PersonalCenterMapper {

    /**
     * 统计我关注的数量
     * @param guId 关注人id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_user_attention where gu_id=${guId} and is_delete=1")
    int selectFollowNum(@Param("guId") int guId);

    /**
     * 统计我的粉丝数量
     * @param bgId 被关注人id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_user_attention where bg_id=${bgId} and is_delete=1")
    int selectFansNum(@Param("bgId") int bgId);

    /**
     * 统计点赞数量
     * @param userId 用户id
     * @return
     */
    @Select("SELECT COALESCE(count(*),0) FROM `tb_circles_give` as a INNER JOIN tb_circles as b on a.zq_id=b.id where b.u_id=${userId} and b.is_delete=1 and a.give_cancel=1")
    int selectGiveNum(@Param("userId") int userId);

    /**
     * 查新我的关注关系表
     * @param guId 当前登录用户id
     * @return
     */
    @Select("select * from tb_user_attention  where gu_id=${guId} and is_delete=1")
    List<Attention> selectFollowByGid(@Param("guId") int guId);

    /**
     * 查询是否有关注
     * @param guId 关注人id
     * @param bgId 被关注人id
     * @return
     */
    @Select("select b.name,b.avatar,b.id,b.introduction from tb_user_attention a INNER JOIN tb_user b on a.gu_id=b.id  where a.gu_id=${guId} and a.bg_id=${bgId} and a.is_delete=1")
    UserMessageVo selectFollowMy(@Param("guId") int guId, @Param("bgId") int bgId);

    /**
     * 查询我收藏的帖子
     * @param userId 用户id
     * @return
     */
    @Select("select a.id,d.id as uId,d.user_name,d.avatar,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId " +
            "from tb_resources a INNER JOIN tb_user d on a.u_id=d.id INNER JOIN tb_tags b on a.tags_two=b.id INNER JOIN tb_user_collection c on a.id=c.t_id  " +
            "where c.u_id=${userId} and a.is_delete=1 and c.is_delete=1")
    List<HomeClassificationVo> queryFavoritePosts(@Param("userId") int userId);


    /**
     * 查询我发布的帖子
     * @param userId 用户id
     * @return
     */
    @Select("select a.id,c.id as uId,c.user_name,c.avatar,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId from tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.u_id=${userId} and a.is_delete=1")
    List<HomeClassificationVo> queryHavePostedPosts(@Param("userId") int userId);

    /**
     * 修改用户信息
     * @param sql 拼接内容
     * @param id 用户id
     * @return
     */
    @Update("update tb_user set ${sql} where id=${id}")
    int updateUserMessage(@Param("sql") String sql,@Param("id") int id);
}
