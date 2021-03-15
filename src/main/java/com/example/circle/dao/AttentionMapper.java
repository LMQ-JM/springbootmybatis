package com.example.circle.dao;

import com.example.circle.entity.Attention;
import com.example.circle.vo.CircleClassificationVo;
import com.example.user.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/6 13:33
 */
@Component
public interface AttentionMapper {


    /**
     * 添加关注信息
     * @param attention 关注对象
     * @return
     */
    @Insert("insert into tb_user_attention(gu_id,bg_id,remarks,create_at)values(${attention.guId},${attention.bgId},#{attention.remarks},#{attention.createAt})")
    int addAttention(@Param("attention") Attention attention);

    /**
     * 查询我是否关注过该用户
     * @param guId 关注人id
     * @param bgId 被关注人id
     * @return
     */
    @Select("select * from tb_user_attention where gu_id=${guId} and bg_id=${bgId}")
    Attention queryWhetherExist(@Param("guId") int guId,@Param("bgId") int bgId);

    /**
     * 查询我是否关注该用户
     * @param guId 关注人id
     * @param bgId 被关注人id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_user_attention where gu_id=${guId} and bg_id=${bgId} and is_delete=1")
    int queryWhetherAttention(@Param("guId") int guId,@Param("bgId") int bgId);

    /**
     * 查询我关注的用户
     * @param guId 关注人id
     * @return
     */
    @Select("select b.id,b.avatar,b.user_name from tb_user_attention a INNER JOIN tb_user b on a.bg_id=b.id where a.gu_id=${guId} and a.is_delete=1")
    List<User> queryPeopleFollow(@Param("guId") int guId);

    /**
     * 修改关注状态
     * @param isDelete （取消关注，关注）
     * @param guId 关注人id
     * @param bgId 被关注人id
     * @return
     */
    @Update("Update tb_user_attention set is_delete=${isDelete}  where gu_id=${guId} and bg_id=${bgId} ")
    int updatePostingFollow(@Param("isDelete") int isDelete,@Param("guId") int guId,@Param("bgId") int bgId);


    /**
     * 查询我关注的人发的帖子
     * @param userId 用户id
     * @return
     */
    @Select("select a.id,a.content,a.tags_one,a.tags_two,a.type,d.id as uId,d.user_name,d.avatar,a.video,a.cover,a.browse,a.create_at,c.tag_name,c.id as tagId from tb_circles a INNER JOIN tb_user d on a.u_id=d.id INNER JOIN tb_user_attention b on a.u_id=b.bg_id INNER JOIN tb_tags c on a.tags_two=c.id where b.gu_id=${userId} and b.is_delete=1 order by a.create_at desc ${paging}")
    List<CircleClassificationVo> queryAttentionPerson(@Param("userId") int userId,@Param("paging") String paging);
}
