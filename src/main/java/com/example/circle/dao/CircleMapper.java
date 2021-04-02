package com.example.circle.dao;

import com.example.circle.entity.Circle;
import com.example.circle.entity.Img;
import com.example.circle.vo.CircleClassificationVo;
import com.example.circle.vo.CircleLabelVo;
import com.example.home.entity.CommunityUser;
import com.example.home.vo.CommunityVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/1/19 16:09
 */
@Component
public interface CircleMapper {

    /**
     *
     * 查询所有圈子的数据
     * @return
     */
    @Select("SELECT a.id,a.content,b.tag_name,a.img,a.type,a.video,a.favour,a.collect,a.browse FROM tb_circles a inner JOIN tb_tags b on a.tags_two=b.id where  a.is_delete=1")
    List<CircleLabelVo> queryAllCircles();

    /**
     * 统计圈子数据
     * @return
     */
    @Select("SELECT COALESCE(count(*),0) FROM tb_circles a inner JOIN tb_tags b on a.tags_two=b.id")
    Integer countAllCircles();

    /**
     * 增加圈子帖子
     * @param circle
     * @return
     */
    @Insert("insert into tb_circles(content,tags_one,tags_two,type,video,cover,create_at,u_id,title,haplont_type)values(#{circle.content},${circle.tagsOne},${circle.tagsTwo},${circle.type},#{circle.video},#{circle.cover},#{circle.createAt},${circle.uId},#{circle.title},${circle.haplontType})")
    @Options(useGeneratedKeys=true, keyProperty="circle.id",keyColumn="id")
    int addCirclePost(@Param("circle") Circle circle);

    /**
     * 添加图片
     * @param img
     * @return
     */
    @Insert("insert into tb_img(z_id,img_url,type,create_at)values(${img.zId},#{img.imgUrl},${img.type},#{img.createAt})")
    int addImg(@Param("img") Img img);

    /**
     * 浏览量加一
     * @param id
     * @return
     */
    @Insert("update tb_circles set browse=browse+1 where id=${id} ")
    int updateBrowse(@Param("id") int id);

    /**
     * 根据条件查询所有圈子
     * @param sql 条件拼接
     * @param paging 分页拼接
     * @return
     */
    @Select("select a.id,a.content,b.tag_name,a.type,a.video,a.favour,a.collect,a.browse,a.title,a.create_at,c.avatar,c.id as uId,c.user_name " +
            "from tb_circles a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tag b on a.tags_one=b.id " +
            "where a.is_delete=1 ${sql} ORDER BY a.create_at desc ${paging}")
    List<CircleLabelVo> selectAllPosting(@Param("sql") String sql,@Param("paging") String paging);

    /**
     * 根据条件统计数量
     * @param sql
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_circles a INNER JOIN tb_tags b on a.tags_two=b.id INNER JOIN tb_user c on a.u_id=c.id where a.is_delete=1 ${sql}")
    Integer selectAllPostingCount(@Param("sql") String sql);

    /**
     * 批量删除
     * @param id
     * @return
     */
    @Update("update tb_circles set is_delete=0  where id = ${id}")
    Integer deletes(@Param("id") int id);

    /**
     * 根据圈子中的标签id查询帖子
     * @param id 一级标签id
     * @param paging 分页
     * @return
     */
    @Select("select a.*,b.tag_name,b.id as tagId,c.avatar,c.id as uId,c.user_name " +
            "from tb_circles a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id  " +
            "where a.tags_one=${id} and a.is_delete=1 order by a.create_at desc  ${paging}")
    List<CircleClassificationVo> selectPostsBasedTagIdCircle(@Param("id") int id, @Param("paging") String paging);


    /**
     * 统计每个一级标签下面有多少个帖子
     * @param id 一级标签id
     * @return
     */
    @Select("select count(if(tags_two=${id},1,null)) from tb_circles  where is_delete=1")
    int countPostsBasedTagIdCircle(@Param("id") int id);


    /**
     * 根据圈子中二级标签id查询帖子
     * @param id 二级标签id
     * @param paging 分页
     * @return
     */
    @Select("select a.*,b.tag_name,b.id as tagId,c.avatar,c.id as uId,c.user_name " +
            "from tb_circles a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id  " +
            "where a.tags_two=${id} and a.is_delete=1 order by a.create_at desc  ${paging}")
    List<CircleClassificationVo> selectPostsBasedTagIdCircleTwo(@Param("id") int id, @Param("paging") String paging);

    /**
     * 查询单个圈子的帖子
     * @param id 帖子id
     * @return
     */
    @Select("select a.*,b.tag_name,b.id as tagId,c.avatar,c.id as uId,c.user_name " +
            "from tb_circles a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id  " +
            "where a.id=${id} and a.is_delete=1")
    CircleClassificationVo querySingleCircle(@Param("id") int id);

    /**
     * 根据标签id查询帖子
     * @param id 社区分类id
     * @param paging 分页
     * @return
     */
    @Select("select a.*,b.id as uId,b.avatar,b.user_name from tb_circles a INNER JOIN tb_user b on a.u_id=b.id where a.tags_two=${id} and a.is_delete=1 ${paging}")
    List<Circle> selectPostsByCommunityCategoryId(@Param("id") int id, @Param("paging") String paging);

    /**
     * 根据圈子id查询圈子信息
     * @param id 标签id
     * @return
     */
    @Select("select a.*,b.user_name,b.id as userId from tb_community a INNER JOIN tb_user b on a.user_id=b.id where a.tag_id=${id} and a.type=1")
    CommunityVo selectCommunityCategoryId(@Param("id") int id);


    /**
     * 加入圈子
     * @param communityUser
     * @return
     */
    @Insert("insert into tb_community_user(community_id,user_id)values(${communityUser.communityId},${communityUser.userId})")
    int joinCircle(@Param("communityUser") CommunityUser communityUser);

    /**
     * 根据三级id查询帖子
     * @param haplontType 单元体id
     * @param paging 分页
     * @return
     */
    @Select("select a.*,c.id as uId,c.avatar,c.user_name,b.tag_name,b.id as tagId from" +
            " tb_circles a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.haplont_type=${haplontType} and a.tags_two=${tagId} and a.is_delete=1 order by a.create_at desc ${paging}")
    List<CircleClassificationVo> queryPostByHaplontType(@Param("haplontType") int haplontType,@Param("paging") String paging,@Param("tagId") int tagId);

    /**
     * 根据圈子内容模糊查询
     * @param content 内容
     * @param paging 分页
     * @return
     */
    @Select("select a.*,c.id as uId,c.avatar,c.user_name,b.tag_name,b.id as tagId from" +
            " tb_circles a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.content like CONCAT('%',#{content},'%') and a.is_delete=1 order by a.create_at desc ${paging}")
    List<CircleClassificationVo> queryFuzzyCircle(@Param("content") String content,@Param("paging") String paging);
}
