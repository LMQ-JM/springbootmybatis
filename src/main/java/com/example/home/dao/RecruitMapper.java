package com.example.home.dao;

import com.example.home.entity.Recruit;
import com.example.home.vo.RecruitVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @create 2021/2/19
 **/
@Component
public interface RecruitMapper {

    /**
     * 查询所有发布的招牌信息
     * @param pagings 分页
     * @return
     */
    @Select("select a.*,b.user_name,b.head_portrait,b.corporate_name from tb_recruit a INNER JOIN tb_recruiting_users b on a.user_id=b.id ${pagings}")
    List<RecruitVo> selectAllRecruit(@Param("pagings") String pagings);
}
