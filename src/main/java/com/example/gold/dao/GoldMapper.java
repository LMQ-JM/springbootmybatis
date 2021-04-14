package com.example.gold.dao;

import com.example.gold.entity.Gold;
import com.example.gold.entity.PostExceptional;
import com.example.gold.entity.UserGoldCoins;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/4/13 14:24
 */
@Component
public interface GoldMapper {

    /**
     * 查询金币
     * @return
     */
    @Select("select * from tb_gold")
    List<Gold> queryGold();

    /**
     * 根据用户id查询自己的总金币数量
     * @param userId 用户id
     * @return
     */
    @Select("select can_withdraw_gold_coins,may_not_withdraw_gold_coins from tb_user_gold_coins where user_id=${userId}")
    UserGoldCoins queryUserGoldNumber(@Param("userId") int userId);

    /**
     * 根据用户id修改用户金币数量
     * @param str
     * @param userId
     * @return
     */
    @Update("update tb_user_gold_coins set ${str} where user_id=${userId}")
    int updateUserGold(@Param("str") String str,@Param("userId") int userId);

    /**
     * 添加帖子打赏数据
     * @param postExceptional
     * @return
     */
    @Insert("insert into tb_post_exceptional(t_id,amount_gold_coins,u_id)values(${postExceptional.tId},${postExceptional.amountGoldCoins},${postExceptional.uId},#{postExceptional.createAt})")
    int addPostExceptional(@Param("postExceptional") PostExceptional postExceptional);


}