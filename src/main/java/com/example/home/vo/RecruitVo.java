package com.example.home.vo;

import com.example.home.entity.RecruitLabel;
import lombok.Data;

import java.util.List;

/**
 * @author MQ
 * @create 2021/2/19
 **/
@Data
public class RecruitVo {

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
     * 发布人id
     */
    private int userId;


    /**
     * 公司人数
     */
    private String numberCompanies;


    /**
     * 工作地址
     */
    private String workAddress;

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
     * 用户名称
     */
    private String userName;

    /**
     * 公司名称
     */
    private String corporateName;

    /**
     * 用户头像
     */
    private String headPortrait;

    /**
     * 岗位要求标签组
     */
    private List<RecruitLabel> recruitLabels;

    /**
     * 是否有效
     */
    private int isDelete;
}
