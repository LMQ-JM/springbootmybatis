package com.example.home.dao;

import com.example.circle.entity.Circle;
import com.example.home.entity.Resources;
import com.example.home.vo.HomeClassificationVo;
import com.example.home.vo.ResourcesLabelVo;
import com.example.home.vo.ResourcesVo;
import com.example.home.vo.ha;
import com.example.tags.entity.Tag;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Administrator
 */
@Component
public interface HomeMapper {

    /**
     * 搜索数据接口
     * @param postingName
     * @param paging
     * @return
     */
    @Select("select * from tb_circles where is_delete=1 and title=#{postingName} ${paging}")
   List<Circle> selectAllSearch(@Param("postingName") String postingName,@Param("paging") String paging);


    /**
     * 查询资源的第一级标签
     * @return
     */
    @Select("select * from tb_tag where type=0")
    List<Tag> selectFirstLevelLabelResource();

    /**
     * 资源市场和学习交流的接口
     * @param id 一级标签id
     * @param paging 分页
     * @return
     */
    //保留  @Select("select a.*,c.name,c.tags_one as cId from tb_resources a INNER JOIN tb_classification c on a.tags_two=c.tags_one where a.tags_one=${id} ${paging}")
    @Select("select a.id,a.u_id,a.user_name,a.avatar,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId from tb_resources a INNER JOIN tb_tags b on a.tags_two=b.id where a.tags_one=${id} order by a.create_at ${paging}")
    List<HomeClassificationVo> selectResourceLearningExchange(@Param("id") int id, @Param("paging") String paging);

    /**
     * 根据社区分类id查询帖子
     * @param id 社区分类id
     * @param paging 分页
     * @return
     */
    @Select("select * from tb_resources where tags_two=${id} ${paging}")
    List<Resources> selectPostsByCommunityCategoryId(@Param("id") int id, @Param("paging") String paging);


    /**
     * 查询单个资源帖子
     * @param id 单个资源帖子id
     * @return
     */
    @Select("select a.id,a.content,a.u_id,a.user_name,a.avatar,a.title,a.favour,a.collect,a.browse,a.create_at,a.type,a.video,b.tag_name,b.id as tagId " +
            "from tb_resources a INNER JOIN tb_tags b on a.tags_two=b.id where a.id=${id}")
    ResourcesVo selectSingleResourcePost(@Param("id") int id);

   /**
    * 根据帖子id查询当前帖子图片
    * @param id
    * @return
    */
    @Select("select img_url from tb_img where z_id=${id}")
    String[] selectImgByPostId(@Param("id") int id);

    /**
     * 浏览量加一
     * @param id
     * @param createAt
     * @return
     */
    @Insert("update tb_resources set browse=browse+1,create_at=#{createAt} where id=${id} ")
    int updateBrowse(@Param("id") int id,@Param("createAt") String createAt);

   /**
    * 增加圈子帖子
    * @param resources
    * @return
    */
   @Insert("insert into tb_resources(content,tags_one,tags_two,type,video,cover,create_at,u_id,user_name,avatar,title)values(#{resources.content},${resources.tagsOne},${resources.tagsTwo},${resources.type},#{resources.video},#{resources.cover},#{resources.createAt},${resources.uId},#{resources.userName},#{resources.avatar},#{resources.title})")
   @Options(useGeneratedKeys=true, keyProperty="resources.id",keyColumn="id")
   int addResourcesPost(@Param("resources") Resources resources);

    /**
     * 根据二级标签id查询推荐的数据
     * @param id 二级标签id
     * @return
     */
    @Select("select a.id,a.u_id,a.user_name,a.avatar,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId " +
            "from tb_resources a INNER JOIN tb_tags b on a.tags_two=b.id where  a.id in (SELECT id FROM (SELECT id FROM tb_resources where  tags_two=${id} ORDER BY RAND()  LIMIT 10) t) ")
   List<HomeClassificationVo> selectRecommendedSecondaryTagId(@Param("id") int id);


    /**
     * 后台
     * 查询所有资源数据
     * @param sql 条件
     * @param paging 分页
     * @return
     */
    @Select("select a.id,a.content,b.tag_name,a.type,a.video,a.favour,a.collect,a.browse,a.title,a.avatar,a.create_at,a.user_name from tb_resources a INNER JOIN tb_tags b on a.tags_two=b.id where a.is_delete=1 ${sql} ORDER BY a.create_at desc ${paging}")
    List<ResourcesLabelVo> selectResourcesAllPosting(@Param("sql") String sql, @Param("paging") String paging);

    /**
     * 根据条件统计数量
     * @param sql
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_resources a INNER JOIN tb_tags b on a.tags_two=b.id where a.is_delete=1 ${sql}")
    Integer selectResourcesAllPostingCount(@Param("sql") String sql);

    /**
     * 批量删除
     * @param id
     * @return
     */
    @Update("update tb_resources set is_delete=0  where id = ${id}")
    Integer deletes(@Param("id") int id);

    @Select("select id,img from tb_resources where user_name='徐老师'")
    List<ha> selectImg();

    /**
     * 批量增加图片
     * @param zId 帖子id
     * @param imgUrl 图片地址
     * @param createAt 创建时间
     * @param postType 帖子类型
     * @return
     */
    //@Insert("insert into tb_img(z_id,img_url,type,create_at) values(${zId}, #{imgUrl},0, #{createAt})")
    @Insert({
         "<script>",
         "insert into tb_img(z_id,img_url,type,create_at) values ",
         "<foreach collection='imgUrl' item='item' index='index' separator=','>",
         "(${zId}, #{item},${postType}, #{createAt})",
         "</foreach>",
         "</script>"
    })
    int addImg(@Param("zId") int zId, @Param("imgUrl") String[] imgUrl,@Param("createAt") String createAt,@Param("postType") int postType);

}
