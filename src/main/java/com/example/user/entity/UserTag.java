package com.example.user.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/2 10:15
 * 用户与标签表的实体类
 */
@Data
public class UserTag {

    private int id;

    /**
     * 用户id
     */
    private int uId;

    /**
     * 标签id
     */
    private String tab;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * (1有效，0无效) 默认1
     */
    private int isDelete;



}
