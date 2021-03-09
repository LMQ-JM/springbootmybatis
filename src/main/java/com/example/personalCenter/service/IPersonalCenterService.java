package com.example.personalCenter.service;

import com.example.home.vo.HomeClassificationVo;
import com.example.personalCenter.vo.InquireFollowersLikesVo;
import com.example.personalCenter.vo.UserMessageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/9 9:32
 */
public interface IPersonalCenterService {

    /**
     * 查询关注 粉丝  获赞数量
     * @param userId 用户id
     * @return
     */
    InquireFollowersLikesVo queryInquireFollowersLikes(int userId);


    /**
     * 查询好友
     * @param userId
     * @return
     */
    List<UserMessageVo> selectFriend(int userId);

    /**
     * 查询我收藏的帖子
     * @param userId 用户id
     * @return
     */
    List<HomeClassificationVo> queryFavoritePosts(@Param("userId") int userId);

    /**
     * 查询我发布的帖子
     * @param userId 用户id
     * @return
     */
    List<HomeClassificationVo> queryHavePostedPosts(int userId);

    /**
     * 修改用户介绍
     * @param introduction 内容
     * @param userId 用户id
     * @return
     */
    int updateUserDataByIntroduction(String introduction,int userId);

    /**
     * 修改用户地址
     * @param domicileProvince 省
     * @param domicileCity 市
     * @param domicileCounty 县
     * @param userId 用户id
     * @return
     */
    int updateUserAddress(String domicileProvince,String domicileCity,String domicileCounty,int userId);

    /**
     * 修改用户头像
     * @param avatar 地址
     * @param userId 用户id
     * @return
     */
    int updateUserAvatar(String avatar, int userId);

    /**
     * 修改用户生日
     * @param birthday 内容
     * @param userId 用户id
     * @return
     */
    int updateUserBirthday(String birthday, int userId);

    /**
     * 修改背景图
     * @param backgroundPicture 地址
     * @param userId 用户id
     * @return
     */
    int updateUserBackgroundPicture(String backgroundPicture, int userId);

    /**
     * 修改用户名称
     * @param name 名称
     * @param userId 用户id
     * @return
     */
    int updateUserName(String name, int userId);

    /**
     * 修改性别
     * @param sex 用户性别（1男，0女）
     * @param userId 用户id
     * @return
     */
    int updateUserSex(String sex, int userId);

    /**
     * 查询我的需求
     * @param userId 用户id
     * @return
     */
    List<String> queryMyNeed(int userId);
}

