package com.example.home.entity;

import lombok.Data;

/**
 * @author MQ
 * @create 2021/2/19
 **/
@Data
public class RecruitingUsers {

    private int id;

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
     * 创建时间
     */
    private String createAt;
}
