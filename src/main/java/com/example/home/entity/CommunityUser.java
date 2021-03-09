package com.example.home.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/3 13:42
 * 圈子用户实体类
 */
@Data
public class CommunityUser {

    private int id;

    /**
     * 圈子id
     */
    private int communityId;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 0资源，1圈子
     */
    private int type;

    /**
     * 创建时间
     */
    private String createAt;
}
