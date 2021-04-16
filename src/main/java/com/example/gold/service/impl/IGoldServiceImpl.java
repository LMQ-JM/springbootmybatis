package com.example.gold.service.impl;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ResultUtil;
import com.example.gold.dao.GoldMapper;
import com.example.gold.entity.PostExceptional;
import com.example.gold.entity.UserGoldCoins;
import com.example.gold.service.IGoldService;
import com.example.gold.vo.UserSignInVo;
import com.example.user.dao.UserMapper;
import com.example.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MQ
 * @date 2021/4/13 14:31
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IGoldServiceImpl implements IGoldService {

    @Autowired
    private GoldMapper goldMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public ResultUtil postExceptional(int rewardedUserId,PostExceptional postExceptional) {
        postExceptional.setCreateAt(System.currentTimeMillis()/1000+"");

        //根据用户id查询自己所有的金币
        UserGoldCoins userGoldCoins = goldMapper.queryUserGoldNumber(postExceptional.getUId());

        //先用可提现的金币进行打赏
        if(userGoldCoins.getCanWithdrawGoldCoins() <postExceptional.getAmountGoldCoins()){
            if(userGoldCoins.getMayNotWithdrawGoldCoins()<postExceptional.getAmountGoldCoins()){
                return ResultUtil.success(null,"你的金币不足",403);
            }

            //修改用户不可提现金币
            int i = goldMapper.updateUserGold("may_not_withdraw_gold_coins=may_not_withdraw_gold_coins-" + postExceptional.getAmountGoldCoins(), postExceptional.getUId());
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"修改金币失败");
            }

            //被打赏人的金币数量增加
            int i2 = goldMapper.updateUserGold("can_withdraw_gold_coins=can_withdraw_gold_coins+" + postExceptional.getAmountGoldCoins(),rewardedUserId);
            if(i2<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"修改金币失败");
            }

            //帖子打赏
            int i1 = goldMapper.addPostExceptional(postExceptional);
            if(i1<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"打赏失败");
            }

            return ResultUtil.success(i1,"成功",200);


        }

        //修改用户可提现金币
        int i = goldMapper.updateUserGold("can_withdraw_gold_coins=can_withdraw_gold_coins-" + postExceptional.getAmountGoldCoins(), postExceptional.getUId());
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改金币失败");
        }

        //被打赏人的金币数量增加
        int i2 = goldMapper.updateUserGold("can_withdraw_gold_coins=can_withdraw_gold_coins+" + postExceptional.getAmountGoldCoins(),rewardedUserId);
        if(i2<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改金币失败");
        }

        //打赏
        int i1 = goldMapper.addPostExceptional(postExceptional);
        if(i1<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"打赏失败");
        }

        return ResultUtil.success(i1,"成功",200);
    }

    @Override
    public void signIn(int userId,int goldNumber) {
        //添加不可提现金币数量
        int i2 = goldMapper.updateUserGold("may_not_withdraw_gold_coins=may_not_withdraw_gold_coins+"+goldNumber,userId);
        if(i2<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改金币失败");
        }


    }

    @Override
    public UserSignInVo queryCheckedInData(Integer userId) {
        UserSignInVo userSignInVo=new UserSignInVo();

        //查询用户头像和名字
        User user = userMapper.queryUserAvatarUserName(userId);
        userSignInVo.setAvatar(user.getAvatar());
        userSignInVo.setUserName(user.getUserName());


        //查询用户金币和签到相关信息
        UserGoldCoins userGoldCoins = goldMapper.queryUserGoldNumber(userId);
        userSignInVo.setUserGoldCoins(userGoldCoins);

        //根据用户查询签到列表
        List<Integer> integers = goldMapper.queryUserSignInByUserId(userId);
        //如果等于空 就初始化 签到数据
        if(integers.size()==0 || integers==null){
            List<Integer> goldList=new ArrayList<>();
            goldList.add(10);
            goldList.add(30);
            goldList.add(50);
            goldList.add(60);
            goldList.add(70);
            goldList.add(80);
            goldList.add(100);
            int i = goldMapper.addSignIn(goldList, userId,System.currentTimeMillis()/1000+"");
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"初始化签到数据失败");
            }
            //初始化后再查询一遍
            integers= goldMapper.queryUserSignInByUserId(userId);
        }

        //set签到数据
        userSignInVo.setListGold(integers);

        return userSignInVo;
    }
}
