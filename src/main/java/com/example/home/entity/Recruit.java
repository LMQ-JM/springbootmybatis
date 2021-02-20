package com.example.home.entity;

import lombok.Data;

/**
 * @author MQ
 * @create 2021/2/19
 **/
@Data
public class Recruit {

    private int id;

    /**
     * 工作名称
     */
    private String jobTitle;

    /**
     * 工资范围
     */
    private String wageRange;

    /**
     * 公司名称
     */
    private String corporateName;

    /**
     * 公司人数
     */
    private String numberCompanies;


    /**
     * 工作地址
     */
    private String workAddress;

    /**
     * 浏览次数
     */
    private int viewCount;

    /**
     * 职位要求
     */
    private String jobRequirements;

    /**
     * 是否需要融资（0不要，1需要）
     */
    private String needFinancing;

    /**
     * (1招全职，2招兼职，3找全职，4找兼职)
     */
    private int type;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效
     */
    private int isDelete;
}
