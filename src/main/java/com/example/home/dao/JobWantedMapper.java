package com.example.home.dao;

import com.example.home.entity.JobWanted;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @date 2021/3/13 15:02
 */
@Component
public interface JobWantedMapper {

    /**
     * 添加求职期望
     * @param jobWanted
     * @return
     */
    @Insert("insert into tb_job_wanted(city,Job_type,expected_position,salary_requirement,create_at)values(#{jobWanted.city},${jobWanted.JobType},#{jobWanted.expectedPosition},#{jobWanted.salaryRequirement},#{jobWanted.createAt})")
    int addJobExpectations(@Param("jobWanted") JobWanted jobWanted);


}
