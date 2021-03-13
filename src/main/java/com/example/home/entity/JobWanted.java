package com.example.home.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/13 14:52
 */
@Data
public class JobWanted {

    private int id;

    /**
     * 求职城市
     */
    private String city;

    /**
     *求职类型
     */
    private int jobType;

    /**
     *期望职位
     */
    private String expectedPosition;

    /**
     *薪资要求
     */
    private String salaryRequirement;

    /**
     *创建时间
     */
    private String createAt;

    /**
     *是否有效
     */
    private int isDelete;
}
