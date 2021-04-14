package com.example.gold.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/4/13 14:27
 * 用户金币表
 */
@Data
public class UserGoldCoins {

    private int id;

    /**
     * 可提现金币
     */
    private int canWithdrawGoldCoins;

    /**
     * 不可提现金币
     */
    private int mayNotWithdrawGoldCoins;

    /**
     * 用户id
     */
    private int userId;


}
