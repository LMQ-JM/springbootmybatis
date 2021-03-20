package com.example.personalCenter.dao;

import com.example.circle.entity.Attention;
import com.example.circle.vo.CircleClassificationVo;
import com.example.home.vo.HomeClassificationVo;
import com.example.personalCenter.vo.CircleVo;
import com.example.personalCenter.vo.UserMessageVo;
import com.example.user.entity.User;
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
    @Select("select b.user_name,b.avatar,b.id,b.introduce from tb_user_attention a INNER JOIN tb_user b on a.gu_id=b.id  where a.gu_id=${guId} and a.bg_id=${bgId} and a.is_delete=1")
    UserMessageVo selectFollowMy(@Param("guId") int guId, @Param("bgId") int bgId);

    /**
     * 查询我收藏的帖子
     * @param userId 用户id
     * @return
     */
    @Select("select a.id,d.id as uId,d.user_name,d.avatar,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId " +
            "from tb_resources a INNER JOIN tb_user d on a.u_id=d.id INNER JOIN tb_tags b on a.tags_two=b.id INNER JOIN tb_user_collection c on a.id=c.t_id  " +
            "where c.u_id=${userId} and a.is_delete=1 and c.is_delete=1 ${paging}")
    List<HomeClassificationVo> queryFavoritePosts(@Param("userId") int userId,@Param("paging") String paging);


    /**
     * 查询我发布资源帖子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select a.content,a.id,c.id as uId,c.user_name,c.avatar,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId from tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.u_id=${userId} and a.is_delete=1 order by a.create_at desc ${paging}")
    List<HomeClassificationVo> queryHavePostedPosts(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 查询我发布圈子帖子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select a.content,a.id,c.id as uId,c.user_name,c.avatar,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId from tb_circles a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.u_id=${userId} and a.is_delete=1 order by a.create_at desc ${paging}")
    List<CircleClassificationVo> queryHavePostedCirclePosts(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 修改用户信息
     * @param sql 拼接内容
     * @param id 用户id
     * @return
     */
    @Update("update tb_user set ${sql} where id=${id}")
    int updateUserMessage(@Param("sql") String sql,@Param("id") int id);

    /**
     * 根据id查询创建的圈子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("SELECT r.id,r.tag_id, r.community_name, r.posters,COUNT(p.community_id) AS cnt FROM tb_community r" +
            " inner JOIN tb_community_user p on r.id = p.community_id where r.user_id=${userId} and r.type=1 GROUP BY p.community_id ${paging}")
    List<CircleVo> myCircleAndCircleJoined(@Param("userId") int userId,@Param("paging") String paging);


    /**
     * 查询我加入的圈子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select b.id,b.tag_id, b.community_name, b.posters from tb_community_user a inner JOIN tb_community b on a.community_id=b.id where a.user_id=${userId} and b.type=1 GROUP BY a.community_id ${paging}")
    List<CircleVo> circleJoined(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 统计每个圈子的人数
     * @param id 用户id
     * @return
     */
    @Select("select count(*) from tb_community_user where community_id=${id}")
    int countCircleJoined(@Param("id") int id);

    /**
     * 查询我近一个月的帖子
     * @param userId 用户id
     * @param type 0资源 1圈子
     * @param sql 表名 可能资源表  可能圈子表
     * @return
     */
    @Select("select b.*,c.id as uId,b.cover,c.avatar,c.user_name,d.tag_name,d.id as tagId from tb_browse a " +
            "INNER JOIN ${sql} b on a.zq_id=b.id INNER JOIN tb_user c on b.u_id=c.id " +
            "INNER JOIN tb_tags d on b.tags_two=d.id where UNIX_TIMESTAMP(DATE_SUB(FROM_UNIXTIME(unix_timestamp(now()),'%Y-%m-%d %H:%i:%s'), INTERVAL 30 DAY))<=a.create_at " +
            "and a.u_id=${userId} and b.is_delete=1 and a.type=${type} GROUP BY a.zq_id ORDER BY a.create_at desc ${paging}")
    List<CircleClassificationVo> queryCheckPostsBeenReadingPastMonth(@Param("userId")int userId, @Param("type")int type,@Param("sql")String sql,@Param("paging") String paging);

    /**
     * 查询观看我的人
     * @param userId 被观看人id
     * @param paging 分页
     * @return
     */
    @Select("select b.id,b.avatar,b.user_name,b.introduce,a.create_at from (select DISTINCT * from tb_viewing_record  ORDER BY create_at desc) a INNER JOIN tb_user b on a.viewers_id=b.id where  a.beholder_id=${userId} GROUP BY a.viewers_id ORDER BY a.create_at desc ${paging}")
    List<User> queryPeopleWhoHaveSeenMe(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 查询观看我的人头像
     * @param userId 被观看人id
     * @return
     */
    @Select("select b.avatar from tb_viewing_record a INNER JOIN tb_user b on a.viewers_id=b.id where to_days(FROM_UNIXTIME(a.create_at)) = to_days(now()) and  a.beholder_id=${userId} GROUP BY a.viewers_id ORDER BY a.create_at desc limit 4")
    String[] queryPeopleWhoHaveSeenMeAvatar(@Param("userId") int userId);

}
