package com.example.home.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/3 16:33
 */
@Data
public class SearchHistory {

    /**
     * 历史内容id
     */
    private int id;

    /**
     * 历史内容
     */
    private String historicalContent;

    /**
     * 用户id
     */
    private int uId;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效（1有效 0无效） 默认1
     */
    private int isDelete;
}
