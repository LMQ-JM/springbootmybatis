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
    @Select("select a.*,c.name,c.id as cId from tb_resources a INNER JOIN tb_tag b on a.tags_one=b.id INNER JOIN tb_classification c on a.tags_two=c.tags_one where a.tags_one=${id} ${paging}")
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
     * 根据社区分类id查询帖子
     * @param id 社区分类id
     * @param paging 分页
     * @return
     */
    @Select("select * from tb_resources where tags_three=${id} ${paging}")
    List<Resources> selectPostsByCommunityCategoryIds(@Param("id") int id, @Param("paging") String paging);


    /**
     *  根据一级标签id查询二级标签
     * @param id 一级标签id
     * @return
     */
    @Select("select * from tb_tags where t_id=${id}")
    List<Tag> selectSecondaryLabel(@Param("id") int id);

   /**
    * 根据二级级标签id查询所有帖子
    * @param id 二级级标签id
    * @param paging 分页
    * @return
    */
   @Select("select a.*,c.name,c.id as cId from tb_resources a  INNER JOIN tb_classification c on a.tags_three=c.tags_one where a.tags_two=${id} ${paging}")
   List<Resources> selectAllPostsSecondaryTagId(@Param("id") int id,@Param("paging") String paging);












    /**
     *  根据二级标签id查询三级级标签
     * @param id 二级标签id
     * @return
     */
    @Select("select * from tb_tagss where t_id=${id}")
    List<Tag> selectPostingOrLabel(@Param("id") int id);

    /**
     * 根据二级标签查询出帖子
     * @param id 二级标签id
     * @param paging 分页
     * @return
     */
    @Select("select a.* from tb_resources a INNER JOIN tb_tags b on a.tags_two=${id} ${paging}")
    List<Resources> selectPosting(@Param("id") int id,@Param("paging") String paging);

    /**
     * 根据三级标签id查询帖子数据
     * @param id 三级标签id
     * @param paging 分页
     * @return
     */
    @Select("select a.* from tb_resources a INNER JOIN tb_tagss b on a.tags_three=${id} b.id= ${paging}")
    List<Resources> selectPostingByThreeLabelId(@Param("id") int id, @Param("paging") Paging paging);

    /**
     * 查询单个资源帖子
     * @param id 单个资源帖子id
     * @return
     */
    @Select("select *from tb_resources where id=${id}")
    Resources selectSingleResourcePost(int id);

   /**
    * 根据帖子id查询当前帖子图片
    * @param id
    * @return
    */
    @Select("select img_url from where z_id=${id}")
    String[] selectImgByPostId(@Param("id") int id);
}
