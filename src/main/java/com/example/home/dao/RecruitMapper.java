package com.example.home.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.home.entity.Recruit;
import com.example.home.entity.RecruitLabel;
import com.example.home.vo.RecruitVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @create 2021/2/19
 **/
@Component
public interface RecruitMapper extends BaseMapper<Recruit> {

    /**
     * 查询所有发布的招牌信息
     * @param paging 分页
     * @param orderBys 排序类型
     * @return
     */
    @Select("select a.*,b.user_name,b.corporate_name,b.company_address,b.number_companies,b.need_financing,c.avatar as headPortrait from tb_recruit a   INNER JOIN tb_recruiting_users b on a.company_id=b.user_id inner join tb_user c on b.user_id=c.id where a.is_delete=1 ${orderBys} ${paging}")
    List<RecruitVo> selectAllRecruit(@Param("paging") String paging,@Param("orderBys") String orderBys);

    /**
     * 根据工作发布表主键id查询这个岗位的要求标签
     * @param id 工作发布表主键id
     * @return List<RecruitLabel>
     */
    @Select("select b.id,b.recruit_label_name from tb_recruit_label_middle a inner join tb_recruit_label b on a.recruit_label_id=b.id where recruit_id=${id}")
    List<RecruitLabel> selectRecruitLabelById(@Param("id") int id);

    /**
     * 根据条件查询人才里面的招牌信息   根据访问量排序
     * @param typeId 类型id
     * @param paging 分页
     * @param orderBy 排序
     * @return List<RecruitVo>
     */
    @Select("select a.*,b.user_name,b.corporate_name,b.company_address,b.number_companies,b.need_financing,c.avatar as headPortrait from tb_recruit a INNER JOIN tb_recruiting_users b on a.company_id=b.user_id inner join tb_user c on b.user_id=c.id where a.type=${typeId} and a.is_delete=1  ${orderBy} ${paging}")
    List<RecruitVo> selectSignboardInformation(@Param("typeId") int typeId, @Param("paging")String paging,@Param("orderBy") String orderBy);

    /**
     * 根据用户id查询自己发布的招聘信息
     * @param userId 用户id
     * @param paging 分页
     * @return List<RecruitVo>
     */
    @Select("select a.*,b.user_name,b.corporate_name,b.company_address,b.number_companies,b.need_financing,c.avatar as headPortrait from tb_recruit a INNER JOIN tb_recruiting_users b on a.company_id=b.user_id inner join tb_user c on b.user_id=c.id " +
            "where a.company_id=${userId} and a.is_delete=1 ORDER BY a.create_at desc ${paging}")
    List<RecruitVo> selectSignboardInformationByUserId(@Param("userId") int userId, @Param("paging")String paging);

    /**
     * 查看招聘信息详情
     * @param id 招聘信息逐渐id
     * @return List<RecruitVo>
     */
    @Select("select a.*,b.user_name,b.corporate_name,b.company_address,b.number_companies,b.need_financing,c.avatar as headPortrait  from tb_recruit a INNER JOIN tb_recruiting_users b on a.company_id=b.user_id inner join tb_user c on b.user_id=c.id" +
            " where a.id=${id} ")
    RecruitVo selectViewDetails(@Param("id") int id);

    /**
     * 增加浏览量
     * @param id 招聘信息逐渐id
     * @return
     */
    @Update("update tb_recruit set view_count=view_count+1 where id=${id}")
    int increasePageViews(@Param("id") int id);

    /**
     * 增加职位和职位标签中间表
     * @param recruitId 职位id
     * @param label 标签id组
     * @return
     */
    @Insert("<script>" +
            "insert into tb_recruit_label_middle(recruit_id,recruit_label_id) VALUES  " +
            "<foreach collection='label' item='item' index='index' separator=','>" +
            "(${recruitId},${item})" +
            "</foreach>" +
            "</script>")
    int addRecruitLabelMiddle(@Param("recruitId") int recruitId,@Param("label") Integer[] label);

    /**
     * 查询职位要求标签
     * @return
     */
    @Select("select * from tb_recruit_label")
    List<RecruitLabel> queryCheckJobRequirementsTab();
}
