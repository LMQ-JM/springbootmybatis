package com.example.home.vo;

import com.example.home.entity.RecruitLabel;
import lombok.Data;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/30 15:58
 */
@Data
public class RecruitJobSearchVo {

    /**
     * 主键
     */
    private int id;

    /**
     * 发布人id
     */
    private int userId;

    /**
     * 职位名称
     */
    private String jobTitle;

    /**
     * 经历内容
     */
    private String jobContent;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 岗位要求标签组
     */
    private List<RecruitLabel> recruitLabels;


}
