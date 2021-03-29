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
     * 工作要求名称
     */
    private String recruitLabelName;

    /**
     * 类型 0 经验  1学历
     */
    private int type;
}
