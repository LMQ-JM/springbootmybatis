package com.example.weChatPay.dao;

import com.example.weChatPay.entity.GoldCoinChange;
import com.example.weChatPay.entity.GoldCoinOrders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * @author MQ
 * @date 2021/4/17 14:37
 */
@Component
public interface OrderMapper {

    /**
     * 添加订单
     * @param goldCoinOrders 订单对象
     * @return
     */
    @Insert("insert into tb_gold_coin_orders(order_number,goods_no,order_status,user_id,money,create_at)" +
            "values(${goldCoinOrders.orderNumber},${goldCoinOrders.goodsNo},${goldCoinOrders.orderStatus},${goldCoinOrders.userId},${goldCoinOrders.money},${goldCoinOrders.createAt})")
    int addOrder(@Param("goldCoinOrders") GoldCoinOrders goldCoinOrders);

    /**
     * 根据订单id修改订单状态
     * @param orderNumber 订单id
     * @param orderStatus 订单状态 0待支付，1已支付,2已取消
     * @return
     */
    @Update("update tb_gold_coin_orders set order_status=1 where order_number=#{orderNumber}")
    int updateOrderStatus(@Param("orderStatus") int orderStatus, @Param("orderNumber") String orderNumber);

    /**
     * 添加用户金币变化
     * @param goldCoinChange
     * @return
     */
    @Insert("insert into tb_gold_coin_change(user_id,source_gold_coin,positive_negative_gold_coins,create_at)" +
            "values(${goldCoinChange.userId},#{goldCoinChange.sourceGoldCoin},#{goldCoinChange.positiveNegativeGoldCoins},#{goldCoinChange.createAt})")
    int addGoldCoinChange(@Param("goldCoinChange") GoldCoinChange goldCoinChange);
}
