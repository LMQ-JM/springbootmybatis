package com.example.home.vo;

import lombok.Data;

/**
 * @author MQ
 * @create 2021/2/24
 **/
@Data
public class ResourcesLabelVo {
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
     * 头像
     */
    private String avatar;

    /**
     * 帖子封面
     */
    private String cover;

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
