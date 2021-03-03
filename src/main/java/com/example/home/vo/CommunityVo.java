package com.example.home.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/3 13:54
 */
@Data
public class CommunityVo {
    private int id;

    /**
     * 社区名称
     */
    private String communityName;

    /**
     * 社区海报
     */
    private String posters;


    /**
     * 圈子介绍
     */
    private String introduce;

    /**
     * 圈子公告
     */
    private String announcement;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效（1有效，0 无效）默认1
     */
    private int isDelete;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 圈子总人数
     */
    private int totalNumberCircles;

    /**
     * 圈子中的用户头像
     */
    private String[] avatar;
}
