package com.example.personalCenter.controller;

import com.example.circle.vo.CircleClassificationVo;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.Paging;
import com.example.home.vo.HomeClassificationVo;
import com.example.personalCenter.service.IPersonalCenterService;
import com.example.personalCenter.vo.CircleVo;
import com.example.personalCenter.vo.InquireFollowersLikesVo;
import com.example.personalCenter.vo.UserMessageVo;
import com.example.user.entity.User;
import com.example.user.entity.UserTag;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/3/9 9:26
 */
@Api(tags = "个人中心API")
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
    public List<HomeClassificationVo> queryFavoritePosts(int userId,Paging paging) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iPersonalCenterService.queryFavoritePosts(userId,paging);
    }

    @ApiOperation(value = "根据id查询他人发布的所有帖子", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryHavePostedPosts")
    public Object queryHavePostedPosts(int othersId,int userId,int type, Paging paging) {
        if(userId==0 || othersId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iPersonalCenterService.queryHavePostedPosts(othersId,userId,type,paging);
    }

    @ApiOperation(value = "查询我的圈子和我加入的圈子", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/myCircleAndCircleJoined")
    public List<CircleVo> myCircleAndCircleJoined(int userId, int type, Paging paging) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iPersonalCenterService.myCircleAndCircleJoined(userId,type,paging);
    }




    @ApiOperation(value = "修改单个介绍", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/updateUserDataByIntroduction")
    public int updateUserDataByIntroduction(String introduction,int userId) throws ParseException {
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
    public int updateUserName(String name, int userId) throws ParseException {
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
    public List<String> queryMyNeed(int othersId) {
        return iPersonalCenterService.queryMyNeed(othersId);
    }

    @ApiOperation(value ="根据用户id查询出我选中的标签", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryTagSelectedBasedUserId")
    public UserTag queryTagSelectedBasedUserId(int userId) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iPersonalCenterService.queryTagSelectedBasedUserId(userId);
    }

    @ApiOperation(value ="查询我近一个月浏览过的帖子", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryCheckPostsBeenReadingPastMonth")
    public List<CircleClassificationVo> queryCheckPostsBeenReadingPastMonth(int userId, int type, Paging paging) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iPersonalCenterService.queryCheckPostsBeenReadingPastMonth(userId,type,paging);
    }

    @ApiOperation(value ="查询看过我的人", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryPeopleWhoHaveSeenMe")
    public List<User> queryPeopleWhoHaveSeenMe(int userId,Paging paging) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iPersonalCenterService.queryPeopleWhoHaveSeenMe(userId,paging);
    }

    @ApiOperation(value ="单独查询观看我的用户头像", notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryViewMyUserProfileSeparately")
    public Map<String,Object> queryViewMyUserProfileSeparately(int userId) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iPersonalCenterService.queryViewMyUserProfileSeparately(userId);
    }



}
