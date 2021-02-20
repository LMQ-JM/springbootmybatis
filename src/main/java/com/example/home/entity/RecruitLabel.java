package com.example.home.entity;

import lombok.Data;

/**
 * @author MQ
 * @create 2021/2/20
 **/
@Data
public class RecruitLabel {

    private int id;

    /**
     *  工作发布表主键id
     */
    private int recruitId;

    /**
     * 工作要求名称
     */
    private String recruitLabelName;
}
