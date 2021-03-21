package com.example.personalCenter.service.impl;

import com.example.circle.dao.AttentionMapper;
import com.example.circle.dao.CircleGiveMapper;
import com.example.circle.dao.CommentMapper;
import com.example.circle.entity.Attention;
import com.example.circle.vo.CircleClassificationVo;
import com.example.circle.vo.CommentUserVo;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.DateUtils;
import com.example.common.utils.Paging;
import com.example.home.dao.HomeMapper;
import com.example.home.dao.RecruitMapper;
import com.example.home.vo.HomeClassificationVo;
import com.example.home.vo.LabelVo;
import com.example.home.vo.RecruitVo;
import com.example.personalCenter.dao.PersonalCenterMapper;
import com.example.personalCenter.service.IPersonalCenterService;
import com.example.personalCenter.vo.CircleVo;
import com.example.personalCenter.vo.InquireFollowersLikesVo;
import com.example.personalCenter.vo.UserMessageVo;
import com.example.user.entity.User;
import com.example.user.entity.UserTag;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private CircleGiveMapper circleGiveMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private RecruitMapper recruitMapper;

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
    public List<HomeClassificationVo> queryFavoritePosts(int userId,Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pag="limit "+page+","+paging.getLimit()+"";
        List<HomeClassificationVo> homeClassificationVos = personalCenterMapper.queryFavoritePosts(userId,pag);
        return homeClassificationVos;
    }

    @Override
    public Object queryHavePostedPosts(int userId, int type, Paging paging) {


        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pag="limit "+page+","+paging.getLimit()+"";

        //查询资源帖子
        if (type == 0) {
            List<HomeClassificationVo> homeClassificationVos = personalCenterMapper.queryHavePostedPosts(userId,pag);
            return homeClassificationVos;
        }
        //查询圈子帖子
        if (type == 1) {
            List<CircleClassificationVo> circles = personalCenterMapper.queryHavePostedCirclePosts(userId,pag);
            for (int i = 0; i < circles.size(); i++) {
                //得到图片组
                String[] strings = homeMapper.selectImgByPostId(circles.get(i).getId());
                circles.get(i).setImg(strings);

                //得到点过赞人的头像
                String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circles.get(i).getId());
                circles.get(i).setGiveAvatar(strings1);

                //得到点赞数量
                Integer integer1 = circleGiveMapper.selectGiveNumber(circles.get(i).getId());
                circles.get(i).setGiveNumber(integer1);


                //等于0在用户没有到登录的情况下 直接设置没有点赞
                if (userId == 0) {
                    circles.get(i).setWhetherGive(0);
                    circles.get(i).setWhetherAttention(0);
                } else {
                    //查看我是否关注了此人
                    int i1 = attentionMapper.queryWhetherAttention(userId, circles.get(i).getUId());
                    if (i1 > 0) {
                        circles.get(i).setWhetherAttention(1);
                    }

                    //查询是否对帖子点了赞   0没有 1有
                    Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                    if (integer > 0) {
                        circles.get(i).setWhetherGive(1);
                    }
                }


                //得到帖子评论数量
                Integer integer2 = commentMapper.selectCommentNumber(circles.get(i).getId());
                circles.get(i).setNumberPosts(integer2);

                //得到评论数据
                List<CommentUserVo> comments = commentMapper.selectComment(circles.get(i).getId());
                circles.get(i).setComments(comments);
            }
            return circles;
        }

        //查询人才帖子
        if (type == 2) {
            List<RecruitVo> recruitVos = recruitMapper.selectSignboardInformationByUserId(userId, pag);
            return recruitVos;
        }

            return null;
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
            int a=0;
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getChecked()=="true"){
                    a++;
                    str.add(list.get(i).getTagName());
                    if(a>2){
                        break;
                    }
                }


            }
        }
        return str;
    }

    @Override
    public List<CircleVo> myCircleAndCircleJoined(int userId, int type, Paging paging) {

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pag="limit "+page+","+paging.getLimit()+"";
        List<CircleVo> circleVos=null;
        if(type==0){
            //查询我创建的圈子
            circleVos= personalCenterMapper.myCircleAndCircleJoined(userId,pag);
            return circleVos;
        }

        //查询我加入的圈子
         circleVos = personalCenterMapper.circleJoined(userId, pag);
        for (int i=0;i<circleVos.size();i++){
            //统计每个圈子的人数
            int i1 = personalCenterMapper.countCircleJoined(circleVos.get(i).getId());
            circleVos.get(i).setCnt(i1);
        }


        return circleVos;
    }

    @Override
    public UserTag queryTagSelectedBasedUserId(int userId) {
        //查询出自己选中的标签
        UserTag userTag=homeMapper.selectOneselfLabel(userId);
        if(userTag==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return userTag;
    }

    @Override
    public List<CircleClassificationVo> queryCheckPostsBeenReadingPastMonth(int userId, int type,Paging paging) {

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pag="limit "+page+","+paging.getLimit()+"";

        String sql="";

        //资源
        if(type==0){
            sql=" tb_resources";
            List<CircleClassificationVo> circleClassificationVos = personalCenterMapper.queryCheckPostsBeenReadingPastMonth(userId, type, sql,pag);
            return circleClassificationVos;
        }

        //圈子
        if(type==1){
            sql=" tb_circles";
            List<CircleClassificationVo> circles = personalCenterMapper.queryCheckPostsBeenReadingPastMonth(userId, type, sql,pag);
            for (int i=0;i<circles.size();i++){
                //得到图片组
                String[] strings = homeMapper.selectImgByPostId(circles.get(i).getId());
                circles.get(i).setImg(strings);

                //得到点过赞人的头像
                String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circles.get(i).getId());
                circles.get(i).setGiveAvatar(strings1);

                //得到点赞数量
                Integer integer1 = circleGiveMapper.selectGiveNumber(circles.get(i).getId());
                circles.get(i).setGiveNumber(integer1);


                //等于0在用户没有到登录的情况下 直接设置没有点赞
                if(userId==0){
                    circles.get(i).setWhetherGive(0);
                    circles.get(i).setWhetherAttention(0);
                }else{
                    //查看我是否关注了此人
                    int i1 = attentionMapper.queryWhetherAttention(userId, circles.get(i).getUId());
                    if(i1>0){
                        circles.get(i).setWhetherAttention(1);
                    }

                    //查询是否对帖子点了赞   0没有 1有
                    Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                    if(integer>0){
                        circles.get(i).setWhetherGive(1);
                    }
                }


                //得到帖子评论数量
                Integer integer2 = commentMapper.selectCommentNumber(circles.get(i).getId());
                circles.get(i).setNumberPosts(integer2);

                //得到评论数据
                List<CommentUserVo> comments = commentMapper.selectComment(circles.get(i).getId());
                circles.get(i).setComments(comments);
            }
            return circles;
        }

        return null;
    }

    @Override
    public List<User> queryPeopleWhoHaveSeenMe(int userId, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pag="limit "+page+","+paging.getLimit()+"";

        //查询看过我的用户信息
        List<User> users = personalCenterMapper.queryPeopleWhoHaveSeenMe(userId, pag);
        users.stream().forEach(u->{
            //得到当前时间戳和过去时间戳比较相隔多少分钟或者多少小时或者都少天或者多少年
            String time = DateUtils.getTime(u.getCreateAt());
            u.setCreateAt(time);
        });

        return users;
    }

    @Override
    public Map<String,Object> queryViewMyUserProfileSeparately(int userId) {
        Map<String,Object> map=new HashMap<>(15);


        //查询看过我的用户信息
        String[] strings = personalCenterMapper.queryPeopleWhoHaveSeenMeAvatar(userId);

        map.put("string",strings);
        map.put("usersSize",strings.length);
        return map;
    }


}
