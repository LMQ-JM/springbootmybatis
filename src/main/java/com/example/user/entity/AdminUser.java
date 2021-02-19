package com.example.user.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/1/20 14:20
 */
@Data
public class AdminUser {

    /**
     * ID
     */
    private int id;

    /**
     * 账号
     */
    private String account;

    /**
     *密码
     */
    private String password;

    /**
     *角色id(1超级管理员，2管理员)
     */
    private int roleId;

    /**
     *头像
     */
    private String avatar;

    /**
     *创建时间
     */
    private String createAt;

    /**
     *修改时间
     */
    private String updateAt;

    /**
     *备注
     */
    private String remarks;

    /**
     *关联前端账号
     */
    private int associatedId;

    /**
     *是否启用 默认启用（1有效，0无效）
     */
    private int isStatus;


}
