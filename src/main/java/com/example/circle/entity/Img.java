package com.example.circle.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/1/22 13:20
 */
@Data
public class Img {

    /**
     * ID
     */
    private int id;

    /**
     * 资源or圈子表唯一号
     */
    private int zId;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 类型(0 资源 1 圈子)
     */
    private int type;

    /**
     * 评论or回复表ID
     */
    private int porId;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否删除（1有效，0无效） 默认1
     */
    private int isDelete;


}
