package com.example.personalCenter.controller;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.home.vo.HomeClassificationVo;
import com.example.personalCenter.service.IPersonalCenterService;
import com.example.personalCenter.vo.InquireFollowersLikesVo;
import com.example.personalCenter.vo.UserMessageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/9 9:26
 */
@Api(tags = "关注API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/PersonalCenterController")
public class PersonalCenterController {

    @Autowired
    private IPersonalCenterService iPersonalCenterService;

    /**
     *
     * 查询关注 粉丝  获赞数量
     * @return
     */
    @ApiOperation(value = "查询关注 粉丝  获赞数量",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryInquireFollowersLikes")
    public InquireFollowersLikesVo queryInquireFollowersLikes(int userId){
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iPersonalCenterService.queryInquireFollowersLikes(userId);
    }

    @ApiOperation(value = "查询好友", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectFriend")
    public List<UserMessageVo> selectFriend(int userId) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iPersonalCenterService.selectFriend(userId);
    }



    @ApiOperation(value = "查询我收藏的帖子", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryFavoritePosts")
    public List<HomeClassificationVo> queryFavoritePosts(int userId) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iPersonalCenterService.queryFavoritePosts(userId);
    }

    @ApiOperation(value = "查询我发布过的帖子", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryHavePostedPosts")
    public List<HomeClassificationVo> queryHavePostedPosts(int userId) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iPersonalCenterService.queryHavePostedPosts(userId);
    }

    @ApiOperation(value = "修改单个介绍", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/updateUserDataByIntroduction")
    public int updateUserDataByIntroduction(String introduction,int userId) {
        int i=iPersonalCenterService.updateUserDataByIntroduction(introduction,userId);
        return i;
    }


    @ApiOperation(value = "修改用户地址", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/updateUserAddress")
    public int updateUserAddress(String domicileProvince,String domicileCity,String domicileCounty,int userId) {
        int n=iPersonalCenterService.updateUserAddress(domicileProvince,domicileCity,domicileCounty, userId);
        return n;
    }


    @ApiOperation(value = "修改用户头像", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/updateUserAvatar")
    public int updateUserAvatar(String avatar, int userId) {
        int updateUserMessage = iPersonalCenterService.updateUserAvatar(avatar, userId);
        return updateUserMessage;
    }

    @ApiOperation(value = "修改用户生日", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/updateUserBirthday")
    public int updateUserBirthday(String birthday, int userId) {
        int updateUserMessage = iPersonalCenterService.updateUserBirthday(birthday, userId);
        return updateUserMessage;
    }

    @ApiOperation(value = "修改用户背景图片", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/updateUserBackgroundPicture")
    public int updateUserBackgroundPicture(String backgroundPicture, int userId) {
        int updateUserMessage = iPersonalCenterService.updateUserBackgroundPicture(backgroundPicture, userId);
        return updateUserMessage;
    }


    @ApiOperation(value = "修改用户名称", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/updateUserName")
    public int updateUserName(String name, int userId) {
        int updateUserMessage = iPersonalCenterService.updateUserName(name, userId);
        return updateUserMessage;
    }


    @ApiOperation(value = "修改用户性别", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/updateUserSex")
    public int updateUserSex(String sex, int userId) {
        int updateUserMessage = iPersonalCenterService.updateUserSex(sex, userId);
        return updateUserMessage;
    }

    @ApiOperation(value = "查询我的需求", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryMyNeed")
    public List<String> queryMyNeed(int userId) {
        return iPersonalCenterService.queryMyNeed(userId);
    }




}
