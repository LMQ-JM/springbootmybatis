package com.example.user.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/2 10:13
 * 用户选择的标签实体类
 */
@Data
public class LoginTag {

    private int id;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * (1有效，0无效) 默认1
     */
    private String isDelete;


    private boolean checked;

    /**
     * 对应tb_tag表中的id
     */
    private int tagId;

}
