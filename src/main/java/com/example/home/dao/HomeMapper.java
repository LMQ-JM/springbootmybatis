package com.example.home.dao;

import com.example.circle.entity.Circle;
import com.example.circle.entity.Img;
import com.example.common.utils.Paging;
import com.example.home.entity.Resources;
import com.example.home.vo.HomeClassificationVo;
import com.example.home.vo.ResourcesVo;
import com.example.home.vo.ha;
import com.example.tags.entity.Tag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
    @Select("select a.id,a.u_id,a.user_name,a.avatar,a.title,a.type,a.video,a.cover,b.tag_name,b.id as tagId from tb_resources a INNER JOIN tb_tags b on a.tags_two=b.id where a.tags_one=${id} order by a.create_at ${paging}")
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
     *浏览量加一
     * @param id
     * @return
     */
    @Insert("update tb_resources set browse=browse+1 where id=${id} ")
    int updateBrowse(@Param("id") int id);

   /**
    * 增加圈子帖子
    * @param resources
    * @return
    */
   @Insert("insert into tb_resources(content,tags_one,tags_two,type,video,cover,create_at,u_id,user_name,avatar,title)values(#{resources.content},${resources.tagsOne},${resources.tagsTwo},${resources.type},#{resources.video},#{resources.cover},#{resources.createAt},${resources.uId},#{resources.userName},#{resources.avatar},#{resources.title})")
   @Options(useGeneratedKeys=true, keyProperty="resources.id",keyColumn="id")
   int addResourcesPost(@Param("resources") Resources resources);




    @Select("select id,img from tb_resources where user_name='徐老师'")
    List<ha> selectImg();

    /*@Insert({
         "<script>",
         "insert into tb_img(z_id,img_url,type,create_at) values ",
         "<foreach collection='list' item='item' index='index' separator=','>",
         "(${zId}, #{item},0, #{createAt})",
         "</foreach>",
         "</script>"
    })*/
    @Insert("insert into tb_img(z_id,img_url,type,create_at) values(${zId}, #{imgUrl},0, #{createAt})")
    int addimg(@Param("zId") int zId, @Param("imgUrl") String imgUrl,@Param("createAt") String createAt);

}
