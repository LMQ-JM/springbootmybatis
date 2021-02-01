package com.example.circle.vo;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class CircleClassificationVo {

    /**
     * ID
     */
    private int id;

    /**
     * 圈子内容
     */
    private String content;

    /**
     * 一级标签ID
     */
    private int tagsOne;

    /**
     * 二级标签ID
     */
    private int tagsTwo;

    /**
     * 图片地址
     */
    private String[] img;

    /**
     *类型（0 图文  1视频）
     */
    private int type;

    /**
     * 用户id
     */
    private int uId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 标题
     */
    private String title;

    /**
     * 视频地址
     */
    private String video;

    /**
     * 封面
     */
    private String cover;

    /**
     * 点赞数量
     */
    private int favour;

    /**
     * 收藏数量
     */
    private int collect;

    /**
     * 浏览记录
     */
    private int browse;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否删除(1有效，0无效)
     */
    private int isDelete;

    /**
     * 社区分类id
     */
    private int cId;

    /**
     * 社区名称
     */
    private String name;
}
