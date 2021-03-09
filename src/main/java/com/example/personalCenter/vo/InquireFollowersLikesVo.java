package com.example.personalCenter.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/9 9:30
 */
@Data
public class InquireFollowersLikesVo {


    /**
     * 关注
     */
    private  int follow;

    /**
     * 粉丝
     */
    private int fans;

    /**
     * 获赞
     */
    private int accolades;
}
