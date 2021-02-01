package com.example.home.dao;

import com.example.circle.entity.Circle;
import com.example.circle.entity.Img;
import com.example.common.utils.Paging;
import com.example.home.entity.Resources;
import com.example.home.vo.HomeClassificationVo;
import com.example.tags.entity.Tag;
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
    @Select("select a.*,c.name,c.tags_one as cId from tb_resources a INNER JOIN tb_classification c on a.tags_two=c.tags_one where a.tags_one=${id} ${paging}")
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
    @Select("select * from tb_resources where id=${id}")
    Resources selectSingleResourcePost(@Param("id") int id);

   /**
    * 根据帖子id查询当前帖子图片
    * @param id
    * @return
    */
    @Select("select img_url from tb_img where z_id=${id}")
    String[] selectImgByPostId(@Param("id") int id);
}
