package com.example.home.dao;

import com.example.home.entity.Resources;
import com.example.home.vo.HomeClassificationVo;
import com.example.home.vo.ResourcesLabelVo;
import com.example.home.vo.ResourcesVo;
import com.example.user.entity.User;
import com.example.user.entity.UserTag;
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
     * @param postingName 内容
     * @param paging 分页
     * @return
     */
    @Select("select * from tb_resources where title like CONCAT('%',#{postingName},'%') and is_delete=1  ${paging}")
   List<Resources> selectAllSearch(@Param("postingName") String postingName,@Param("paging") String paging);



    /**
     * 根据一级标签id查询所有下面的数据
     * @param id 一级标签id
     * @param paging 分页
     * @return
     */
    @Select("select a.id,c.avatar,c.id as uId,c.user_name,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId from tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id " +
            "where a.tags_one=${id} and a.is_delete=1 order by a.create_at desc ${paging}")
    List<HomeClassificationVo> selectResourceLearningExchange(@Param("id") int id, @Param("paging") String paging);

    /**
     * 根据社区分类id查询帖子
     * @param id 社区分类id
     * @param paging 分页
     * @return
     */
    @Select("select a.*,b.id as uId,b.avatar,b.user_name from tb_resources a INNER JOIN tb_user b on a.u_id=b.id " +
            " where a.tags_two=${id} and a.is_delete=1 ${sql} ${paging}")
    List<HomeClassificationVo> selectPostsByCommunityCategoryId(@Param("id") int id, @Param("paging") String paging,@Param("sql") String sql);


    /**
     * 查询单个资源帖子
     * @param id 单个资源帖子id
     * @return
     */
    @Select("select a.id,a.content,c.avatar,c.id as uId,c.user_name,a.title,a.favour,a.collect,a.browse,a.create_at,a.type,a.video,b.tag_name,b.id as tagId " +
            "from tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.id=${id}")
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
     * @return
     */
    @Insert("update tb_resources set browse=browse+1 where id=${id} ")
    int updateBrowse(@Param("id") int id);

   /**
    * 增加资源帖子
    * @param resources
    * @return
    */
   @Insert("insert into tb_resources(content,tags_one,tags_two,type,video,cover,create_at,u_id,title,haplont_type)values(#{resources.content},${resources.tagsOne},${resources.tagsTwo},${resources.type},#{resources.video},#{resources.cover},#{resources.createAt},${resources.uId},#{resources.title},${resources.haplontType})")
   @Options(useGeneratedKeys=true, keyProperty="resources.id",keyColumn="id")
   int addResourcesPost(@Param("resources") Resources resources);

    /**
     * 根据二级标签id查询推荐的数据
     * @param id 二级标签id
     * @return
     */
    @Select("select a.id,c.id as uId,c.user_name,c.avatar,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId " +
            "from tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where  a.id in (SELECT id FROM (SELECT id FROM tb_resources where  tags_two=${id} and is_delete=1 ORDER BY RAND()  LIMIT 10) t) ")
   List<HomeClassificationVo> selectRecommendedSecondaryTagId(@Param("id") int id);


    /**
     * 后台
     * 查询所有资源数据
     * @param sql 条件
     * @param paging 分页
     * @return
     */
    @Select("select a.id,a.content,b.tag_name,a.type,a.video,a.favour,a.collect,a.browse,a.title,a.create_at,c.avatar,c.id as uId,c.user_name " +
            "from tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id " +
            "where a.is_delete=1 ${sql} ORDER BY a.create_at desc ${paging}")
    List<ResourcesLabelVo> selectResourcesAllPosting(@Param("sql") String sql, @Param("paging") String paging);

    /**
     * 根据条件统计数量
     * @param sql
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.is_delete=1 ${sql}")
    Integer selectResourcesAllPostingCount(@Param("sql") String sql);

    /**
     * 批量删除
     * @param id
     * @return
     */
    @Update("update tb_resources set is_delete=0  where id = ${id}")
    Integer deletes(@Param("id") int id);


    /**
     * 批量增加图片
     * @param zId 帖子id
     * @param imgUrl 图片地址
     * @param createAt 创建时间
     * @param postType 帖子类型
     * @return
     */
    @Insert({
         "<script>",
         "insert into tb_img(z_id,img_url,type,create_at) values ",
         "<foreach collection='imgUrl' item='item' index='index' separator=','>",
         "(${zId}, #{item},${postType}, #{createAt})",
         "</foreach>",
         "</script>"
    })
    int addImg(@Param("zId") int zId, @Param("imgUrl") String[] imgUrl,@Param("createAt") String createAt,@Param("postType") int postType);

    /**
     * 查询合作 资源 学习的帖子
     * @param paging 分页
     * @return
     */
    @Select("select a.id,c.id as uId,c.avatar,c.user_name,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId from" +
            " tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.tags_one in (12,13,15) ORDER BY browse desc ${paging}")
    List<HomeClassificationVo>  selectRandom(@Param("paging") String paging);


    /**
     * 查询自己的标签
     * @param userId 用户id
     * @return
     */
    @Select("select *from tb_user_tag where u_id=${userId}")
    UserTag selectOneselfLabel(@Param("userId") int userId);

   /**
    * 根据资源的一级的标签id查询贴
    * @param list
    * @param sql 分页
    * @return
    */
    @Select({
         "<script>" +
                 "select a.id,c.id as uId,c.avatar,c.user_name,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId from" +
                 "  tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.tags_one in"+
                 "<foreach item = 'item' index = 'index' collection = 'list' open='(' separator=',' close=')'>" +
                 "#{item}" +
                 "</foreach> ORDER BY a.create_at desc ${sql}" +
                 "</script>"})
    List<HomeClassificationVo> selectPostByTagOne(@Param("list") List<Integer> list,@Param("sql") String sql);



    /**
     * 根据标签id多表联查出用户数据
     * @param list
     * @return
     */
    @Select({
            "<script>" +
                    "select b.id,b.user_name,b.avatar from" +
                    "  tb_resources a INNER JOIN tb_user b on a.u_id=b.id where a.tags_one in"+
                    "<foreach item = 'item' index = 'index' collection = 'list' open='(' separator=',' close=')'>" +
                    "#{item}" +
                    "</foreach> GROUP BY  b.id" +
                    "</script>"})
    List<User> selectUserByTagOne(@Param("list") List<Integer> list);

    /**
     * 根据三级标签查询资源数据
     * @param haplontType
     * @return
     */
    @Select("select a.id,c.id as uId,c.avatar,c.user_name,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId from" +
            " tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.haplont_type=${haplontType} and a.tags_two=${tagId} order by a.create_at desc ${paging}")
    List<HomeClassificationVo> queryPostByHaplontType(@Param("haplontType") int haplontType,@Param("paging") String paging,@Param("tagId") int tagId);

}
