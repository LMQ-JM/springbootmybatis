package com.example.gold.dao;

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
     * 根据用户id查询自己的总金币数量
     * @param userId 用户id
     * @return
     */
    @Select("select can_withdraw_gold_coins,may_not_withdraw_gold_coins,sign_in_get_gold_coins,consecutive_number,Last_check_in_time from tb_user_gold_coins where user_id=${userId}")
    UserGoldCoins queryUserGoldNumber(@Param("userId") int userId);

    /**
     * 根据用户id修改用户金币数量
     * @param str 表明
     * @param userId 用户名
     * @return
     */
    @Update("update tb_user_gold_coins set ${str} where user_id=${userId}")
    int updateUserGold(@Param("str") String str,@Param("userId") int userId);

    /**
     * 根据用户id修改上次签到时间，连续签到天数，签到得到的金币，不可用金币数量
     * @param userId 用户id
     * @param goldNumber 签到得到的数量
     * @param createAt 创建时间
     * @return
     */
    @Update("update tb_user_gold_coins " +
            "set may_not_withdraw_gold_coins=may_not_withdraw_gold_coins+${goldNumber},consecutive_number=consecutive_number+1,sign_in_get_gold_coins=sign_in_get_gold_coins+${goldNumber},Last_check_in_time=#{createAt} where user_id=${userId}")
    int updateUserGoldSignIn(@Param("userId") int userId,@Param("goldNumber") int goldNumber,@Param("createAt") String createAt);

    /**
     * 添加帖子打赏数据
     * @param postExceptional
     * @return
     */
    @Insert("insert into tb_post_exceptional(t_id,amount_gold_coins,u_id,create_at)values(${postExceptional.tId},${postExceptional.amountGoldCoins},${postExceptional.uId},#{postExceptional.createAt})")
    int addPostExceptional(@Param("postExceptional") PostExceptional postExceptional);

    /**
     * 根据用户id查询金币的的数量
     * @param userId 用户id
     * @return
     */
    @Select("select gold_number from tb_user_sign_in where user_id=${userId}")
    List<Integer> queryUserSignInByUserId(@Param("userId") Integer userId);

    /**
     * 初始化 签到数据
     * @param testLists
     * @param userId
     * @return
     */
    @Insert({
            "<script>",
            "insert into tb_user_sign_in(gold_number, user_id,create_at) values ",
            "<foreach collection='testLists' item='item' index='index' separator=','>",
            "(${item}, ${userId},#{createAt})",
            "</foreach>",
            "</script>"
    })
    int addSignIn(@Param("testLists") List<Integer> testLists,@Param("userId") int userId,@Param("createAt") String createAt);

    /**
     * 初始化用户的金币数量和签到部分数据
     * @param userId
     * @return
     */
    @Insert("insert into tb_user_gold_coins(user_id)values(${userId})")
    int addUserGoldCoins(@Param("userId") int userId);

}
