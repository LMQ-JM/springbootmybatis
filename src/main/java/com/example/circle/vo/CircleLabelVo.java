package com.example.circle.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/1/19 17:25
 */
@Data
public class CircleLabelVo {

    /**
     * id
     */
    private int id;

    /**
     * 内容
     */
    private String content;

    /**
     * 发帖人
     */
    private String userName;

    /**
     * 标签名字
     */
    private String tagName;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片数组
     */
    private String[] img;

    /**
     * 类型（0 图文  1视频）
     */
    private int type;

    /**
     * 视频
     */
    private String video;

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
}
