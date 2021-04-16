package com.example.personalCenter.service;

import com.example.circle.vo.CircleClassificationVo;
import com.example.common.utils.Paging;
import com.example.home.vo.HomeClassificationVo;
import com.example.personalCenter.vo.CircleVo;
import com.example.personalCenter.vo.InquireFollowersLikesVo;
import com.example.personalCenter.vo.UserMessageVo;
import com.example.user.entity.User;
import com.example.user.entity.UserTag;
import org.apache.ibatis.annotations.Param;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/3/9 9:32
 */
public interface IPersonalCenterService {

    /**
     * 查询关注 粉丝  获赞数量
     * @param othersId 用户id
     * @return
     */
    InquireFollowersLikesVo queryInquireFollowersLikes(int othersId);


    /**
     * 查询好友
     * @param userId
     * @return
     */
    List<UserMessageVo> selectFriend(int userId);

    /**
     * 查询我收藏的帖子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    List<HomeClassificationVo> queryFavoritePosts(@Param("userId") int userId,Paging paging);

    /**
     * 根据id查询他人发布的所有帖子
     * @param othersId 他人用户id
     * @param userId 当前登录用户id
     * @param type 0 资源 1圈子 2人才
     * @param paging 分页
     * @return
     */
    Object queryHavePostedPosts(int othersId,int userId,int type, Paging paging);

    /**
     * 修改用户介绍
     * @param introduction 内容
     * @param userId 用户id
     * @return
     */
    int updateUserDataByIntroduction(String introduction,int userId) throws ParseException;

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
    int updateUserName(String name, int userId) throws ParseException;

    /**
     * 修改性别
     * @param sex 用户性别（1男，0女）
     * @param userId 用户id
     * @return
     */
    int updateUserSex(String sex, int userId);

    /**
     * 查询我的需求
     * @param othersId 他人用户id
     * @return
     */
    List<String> queryMyNeed(int othersId);

    /**
     * 查询我的圈子和我加入的圈子
     * @param userId 用户id
     * @param type 类型 0我的圈子 1我加入的圈子
     * @param paging 分页
     * @return
     */
    List<CircleVo> myCircleAndCircleJoined(int userId, int type, Paging paging);

    /**
     * 根据用户id查询出我选中的标签
     * @param userId
     * @return
     */
    UserTag queryTagSelectedBasedUserId(int userId);


    /**
     * 查询我近一个月浏览过的帖子
     * @param userId 用户id
     * @param type 0资源  1圈子
     * @param paging 分页
     * @return
     */
    List<CircleClassificationVo> queryCheckPostsBeenReadingPastMonth(int userId, int type, Paging paging);

    /**
     * 查询看过我的人
     * @param userId 被看人用户id
     * @param paging 分页
     * @return
     */
    List<User> queryPeopleWhoHaveSeenMe(int userId, Paging paging);

    /**
     * 单独查询观看我的用户头像
     * @param userId 被看人用户id
     * @return
     */
    Map<String,Object> queryViewMyUserProfileSeparately(int userId);
}

