package com.example.circle.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/6 13:21
 * 关注实体类
 */
@Data
public class Attention {

    private int id;

    /**
     * 关注人id
     */
    private int guId;

    /**
     *被关注人id
     */
    private int bgId;

    /**
     *备注
     */
    private String remarks;

    /**
     *创建时间
     */
    private String createAt;

    /**
     *是否删除(1有效，0无效) 默认1
     */
    private int isDelete;
}
