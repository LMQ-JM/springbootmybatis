package com.example.gold.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author MQ
 * @date 2021/4/13 14:25
 * 金币实体类
 */
@Data
public class Gold {

    private int id;

    /**
     * 金币数量
     */
    private int goldNumber;

    /**
     * 金币对应的价格
     */
    private BigDecimal goldPrices;
}
