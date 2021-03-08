package com.example.circle.service.impl;

import com.example.circle.dao.AttentionMapper;
import com.example.circle.dao.CircleGiveMapper;
import com.example.circle.dao.CommentMapper;
import com.example.circle.entity.Attention;
import com.example.circle.service.IAttentionService;
import com.example.circle.vo.CircleClassificationVo;
import com.example.circle.vo.CommentUserVo;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.home.dao.HomeMapper;
import com.example.home.vo.LabelVo;
import com.example.user.dao.UserMapper;
import com.example.user.entity.User;
import com.example.user.entity.UserTag;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author MQ
 * @date 2021/3/6 13:26
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AttentionServiceImpl implements IAttentionService {


    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CircleGiveMapper circleGiveMapper;

    @Override
    public int addAttention(Attention attention) {

        if(attention.getGuId()==attention.getBgId()){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"自己不能关注自己哦");
        }

        //查询是否关注了他人
        int i=0;
        Attention attention1 = attentionMapper.queryWhetherExist(attention.getGuId(), attention.getBgId());
        if (attention1!= null) {
            //如果当前状态是1关注的  在进这个判断就是修改为0不关注的状态
            if(attention1.getIsDelete()==1){
                //修改关注状态 为取消关注
                 i= attentionMapper.updatePostingFollow(0, attention.getGuId(), attention.getBgId());
            }
            if(attention1.getIsDelete()==0){
                //修改关注状态 为关注
                 i = attentionMapper.updatePostingFollow(1, attention.getGuId(), attention.getBgId());
            }

        }else{
            //添加关注信息
            attention.setCreateAt(System.currentTimeMillis()/1000+"");
             i = attentionMapper.addAttention(attention);
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
        }

        return i;

    }

    @Override
    public List<User> queryAttentionPerson(int userId) {
        Map<String,Object> map=new HashMap<>(15);

        //查询我关注的人
        List<User> users = attentionMapper.queryPeopleFollow(userId);

        return users;
    }

    @Override
    public List<User> queryRecommendedUserData(int userId) {

        //id组
        List<Integer> idArr=new ArrayList<>();

        UserTag userTag=homeMapper.selectOneselfLabel(userId);
        if(userTag==null){
            List<User> users = userMapper.selectRandom();
            for (int i=0;i<users.size();i++){
                if(users.get(i).getId()==userId){
                    users.remove(i);
                }
            }
            //从list集合随机冲去5条数据
            List<User> randomList = getRandomList(users, 5);
            return  randomList;
        }


        if(userTag!=null){
            JSONArray j= JSONArray.fromObject(userTag.getTab());
            List<LabelVo> list = JSONArray.toList(j, LabelVo.class);
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getChecked()=="true"){
                    idArr.add(list.get(i).getTagId());
                }
            }
        }
        //根据标签id多表联查出用户数据
        List<User> users = homeMapper.selectUserByTagOne(idArr);
        for (int i=0;i<users.size();i++){
            if(users.get(i).getId()==userId){
                users.remove(i);
            }
        }
        //从list集合随机冲去5条数据
        List<User> randomList = getRandomList(users, 5);

        return randomList;
    }

    @Override
    public List<CircleClassificationVo> queryPostsPeopleFollow(int userId) {
        //查询我关注的人发的帖子
        List<CircleClassificationVo> circleClassificationVos = attentionMapper.queryAttentionPerson(userId);
        for (int i=0;i<circleClassificationVos.size();i++){

            //得到图片组
            String[] strings = homeMapper.selectImgByPostId(circleClassificationVos.get(i).getId());
            circleClassificationVos.get(i).setImg(strings);

            //得到点过赞人的头像
            String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circleClassificationVos.get(i).getId());
            circleClassificationVos.get(i).setGiveAvatar(strings1);

            //得到点赞数量
            Integer integer1 = circleGiveMapper.selectGiveNumber(circleClassificationVos.get(i).getId());
            circleClassificationVos.get(i).setGiveNumber(integer1);


            //等于0在用户没有到登录的情况下 直接设置没有点赞
            if(userId==0){
                circleClassificationVos.get(i).setWhetherGive(0);
                circleClassificationVos.get(i).setWhetherAttention(0);
            }else{

                //查询是否对帖子点了赞   0没有 1有
                Integer integer = circleGiveMapper.whetherGive(userId, circleClassificationVos.get(i).getId());
                if(integer>0){
                    circleClassificationVos.get(i).setWhetherGive(1);
                }
            }


            //得到帖子评论数量
            Integer integer2 = commentMapper.selectCommentNumber(circleClassificationVos.get(i).getId());
            circleClassificationVos.get(i).setNumberPosts(integer2);

            //得到评论数据
            List<CommentUserVo> comments = commentMapper.selectComment(circleClassificationVos.get(i).getId());
            circleClassificationVos.get(i).setComments(comments);

            //将所有关注状态为1关注状态
            circleClassificationVos.get(i).setWhetherAttention(1);
        }

        return circleClassificationVos;
    }

    /**
     * @Description list 随机取数据
     * @params     list    list集合
     *           num     随机取多少条
     **/
    public static List<User> getRandomList(List<User> list, int num) {
        List<User> olist = new ArrayList<>();
        if (list.size() <= num) {
            return list;
        } else {
            Random random = new Random();
            for (int i = 0 ;i<num;i++){
                int intRandom = random.nextInt(list.size() - 1);
                olist.add(list.get(intRandom));
                list.remove(list.get(intRandom));
            }
            return olist;
        }
    }
}
