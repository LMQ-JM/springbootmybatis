package com.example.personalCenter.service.impl;

import com.example.circle.entity.Attention;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.home.dao.HomeMapper;
import com.example.home.vo.HomeClassificationVo;
import com.example.home.vo.LabelVo;
import com.example.personalCenter.dao.PersonalCenterMapper;
import com.example.personalCenter.service.IPersonalCenterService;
import com.example.personalCenter.vo.InquireFollowersLikesVo;
import com.example.personalCenter.vo.UserMessageVo;
import com.example.user.entity.UserTag;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MQ
 * @date 2021/3/9 9:33
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PersonalCenterServiceImpl implements IPersonalCenterService {


    @Autowired
    private PersonalCenterMapper personalCenterMapper;

    @Autowired
    private HomeMapper homeMapper;

    @Override
    public InquireFollowersLikesVo queryInquireFollowersLikes(int userId) {

        //关注
        int i = personalCenterMapper.selectFollowNum(userId);

        //粉丝
        int i1 = personalCenterMapper.selectFansNum(userId);

        //获赞
        int i2 = personalCenterMapper.selectGiveNum(userId);


        InquireFollowersLikesVo inquireFollowersLikesVo=new InquireFollowersLikesVo();
        inquireFollowersLikesVo.setFollow(i);
        inquireFollowersLikesVo.setFans(i1);
        inquireFollowersLikesVo.setAccolades(i2);

        return inquireFollowersLikesVo;
    }

    @Override
    public List<UserMessageVo> selectFriend(int userId) {
        List<UserMessageVo> list=new ArrayList<>();

        //查询出我关注的人
        List<Attention> followList=personalCenterMapper.selectFollowByGid(userId);
        if(followList.size()!=0){
            for (int i = 0; i < followList.size(); i++) {
                //然后再用我关注人的id 查询他关注人的id等不等与我 ，如果等于我 就查出他的用户信息
                UserMessageVo myFollow=personalCenterMapper.selectFollowMy(followList.get(i).getBgId(),userId);
                if(myFollow!=null){
                    list.add(myFollow);
                }
            }
        }
        return list;
    }

    @Override
    public List<HomeClassificationVo> queryFavoritePosts(int userId) {
        List<HomeClassificationVo> homeClassificationVos = personalCenterMapper.queryFavoritePosts(userId);
        return homeClassificationVos;
    }

    @Override
    public List<HomeClassificationVo> queryHavePostedPosts(int userId) {
        List<HomeClassificationVo> homeClassificationVos = personalCenterMapper.queryHavePostedPosts(userId);
        return homeClassificationVos;
    }

    @Override
    public int updateUserDataByIntroduction(String introduction, int id) {
        String sql=" introduce='"+introduction+"'";

        int i=personalCenterMapper.updateUserMessage(sql,id);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return i;
    }

    @Override
    public int updateUserAddress(String domicileProvince, String domicileCity, String domicileCounty, int id) {
        String sql=" curr_province='"+domicileProvince+"',city='"+domicileCity+"',county='"+domicileCounty+"'";

        int n=personalCenterMapper.updateUserMessage(sql, id);
        if(n<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        return n;
    }

    @Override
    public int updateUserAvatar(String avatar, int id) {
        String sql=" avatar='"+avatar+"'";
        int n=personalCenterMapper.updateUserMessage(sql, id);
        if(n<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return n;
    }

    @Override
    public int updateUserBirthday(String birthday, int id) {

        String sql=" birthday='"+birthday+"'";

        int updateUserMessage = personalCenterMapper.updateUserMessage(sql, id);
        if(updateUserMessage<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return updateUserMessage;
    }

    @Override
    public int updateUserBackgroundPicture(String backgroundPicture, int id) {

        String sql=" picture='"+backgroundPicture+"'";

        int updateUserMessage = personalCenterMapper.updateUserMessage(sql, id);
        if(updateUserMessage<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return updateUserMessage;
    }

    @Override
    public int updateUserName(String name, int id) {
        String sql=" user_name='"+name+"'";

        int updateUserMessage = personalCenterMapper.updateUserMessage(sql, id);
        if(updateUserMessage<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return updateUserMessage;
    }

    @Override
    public int updateUserSex(String sex, int id) {
        String sql=" user_sex='"+sex+"'";

        int updateUserMessage = personalCenterMapper.updateUserMessage(sql, id);
        if(updateUserMessage<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return updateUserMessage;
    }

    @Override
    public List<String> queryMyNeed(int userId) {

        List<String> str=new ArrayList<>();

        UserTag userTag=homeMapper.selectOneselfLabel(userId);

        if(userTag!=null){
            JSONArray j= JSONArray.fromObject(userTag.getTab());
            List<LabelVo> list = JSONArray.toList(j, LabelVo.class);
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getChecked()=="true"){
                    str.add(list.get(i).getTagName());
                }
            }
        }
        return str;
    }


}
