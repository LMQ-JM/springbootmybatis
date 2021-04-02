package com.example.home.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.home.entity.RecruitJobSearch;
import com.example.home.entity.RecruitLabel;
import com.example.home.vo.RecruitJobSearchVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/30 16:13
 */
@Component
public interface RecruitJobSearchMapper extends BaseMapper<RecruitJobSearch> {

    /**
     * 根据条件查询人才里面的找工作信息
     * @param typeId 类型id
     * @param paging 分页
     * @param orderBy 0最热 1最新
     * @return
     */
    @Select("select a.*,b.user_name,b.avatar from tb_recruit_job_search a  inner join tb_user b on a.user_id=b.id where a.type=${typeId} and a.is_delete=1  ${orderBy} ${paging}")
    List<RecruitJobSearchVo> queryJobInformation(@Param("typeId") int typeId, @Param("paging") String paging, @Param("orderBy") String orderBy);

    /**
     * 根据工作发布表主键id查询这个岗位的要求标签
     * @param id 工作发布表主键id
     * @return List<RecruitLabel>
     */
    @Select("select b.id,b.recruit_label_name from tb_recruit_job_search_label_middle a inner join tb_recruit_label b on a.recruit_label_id=b.id where recruit_id=${id}")
    List<RecruitLabel> queryRecruitJobSearchLabelById(@Param("id") int id);


    /**
     * 增加职位和职位标签中间表
     * @param recruitId 职位id
     * @param label 标签id组
     * @return
     */
    @Insert("<script>" +
            "insert into tb_recruit_job_search_label_middle(recruit_id,recruit_label_id) VALUES  " +
            "<foreach collection='label' item='item' index='index' separator=','>" +
            "(${recruitId},${item})" +
            "</foreach>" +
            "</script>")
    int addRecruitJobSearchLabelMiddle(@Param("recruitId") int recruitId,@Param("label") Integer[] label);


    /**
     * 查看找工作信息详情
     * @param id 主键id
     * @return
     */
    @Select("select a.*,b.user_name,b.avatar from tb_recruit_job_search a INNER JOIN tb_users b on a.user_id=b.id" +
            " where a.id=${id} ")
    RecruitJobSearchVo queryJobSearchDetails(@Param("id") int id);
}
