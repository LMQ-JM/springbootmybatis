package com.example.tags.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/1/21 16:01
 */
@Data
public class Tag {

    /**
     * ID
     */
    private int id;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 一级标签ID
     */
    private int tId;

    /**
     * 型(0 资源 1 圈子)
     */
    private int type;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是有有效（1有效，0无效）
     */
    private int isDelete;

    /**
     * 图标地址
     */
    private String imgUrl;

    /**
     * 这个标签发帖数量
     */
    private int num;


}
