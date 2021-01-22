package com.example.user.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/1/16 16:26
 */
@Data
public class UserHtVo {
    /**
     * 用户id
     */
    private int id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户性别
     */
    private int sex;

    /**
     * 用户注册时间
     */
    private String createAt;


}
