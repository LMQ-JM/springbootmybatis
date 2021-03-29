package com.example.home.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.home.entity.RecruitingUsers;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @date 2021/3/29 15:20
 */
@Component
public interface CompanyMapper extends BaseMapper<RecruitingUsers> {


    /**
     * 查询是否注册过公司
     * @param userId
     * @return
     */
    @Select("select count(*) from tb_recruiting_users where user_id=${userId}")
    int queryCount(@Param("userId") int userId);

}
