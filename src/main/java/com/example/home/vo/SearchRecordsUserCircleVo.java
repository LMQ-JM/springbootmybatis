package com.example.home.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/3 16:27
 */
@Data
public class SearchRecordsUserCircleVo {

    /**
     * 历史内容id
     */
    private int id;

    /**
     * 历史内容
     */
    private String historicalContent;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效（1有效 0无效） 默认1
     */
    private int isDelete;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;
}
