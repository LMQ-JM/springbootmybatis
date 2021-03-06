package com.example.circle.service.impl;

import com.example.circle.dao.AttentionMapper;
import com.example.circle.dao.CircleGiveMapper;
import com.example.circle.dao.CircleMapper;
import com.example.circle.dao.CommentMapper;
import com.example.circle.entity.Circle;
import com.example.circle.entity.Give;
import com.example.circle.entity.Img;
import com.example.circle.service.ICircleService;
import com.example.circle.vo.CircleClassificationVo;
import com.example.circle.vo.CommentUserVo;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.*;
import com.example.home.dao.BrowseMapper;
import com.example.home.dao.CommunityMapper;
import com.example.home.dao.HaplontMapper;
import com.example.home.dao.HomeMapper;
import com.example.home.entity.Browse;
import com.example.home.entity.CommunityUser;
import com.example.home.entity.Haplont;
import com.example.home.entity.Resources;
import com.example.home.vo.CommunityVo;
import com.example.learn.dao.QuestionMapper;
import com.example.tags.dao.TagMapper;
import com.example.tags.entity.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MQ
 * @date 2021/1/19 16:10
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CircleServiceImpl implements ICircleService {

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private CircleGiveMapper circleGiveMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private CommunityMapper communityMapper;

    @Autowired
    private HaplontMapper haplontMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private BrowseMapper browseMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<CircleClassificationVo> queryImagesOrVideos(int type,Paging paging,int userId) {
        Integer pages=(paging.getPage()-1)*paging.getLimit();
        String pagings=" limit "+pages+","+paging.getLimit()+"";

        List<CircleClassificationVo> circles = circleMapper.queryImagesOrVideos(type, pagings);
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

            //将时间戳转换为多少天或者多少个小时和多少年
            String time = DateUtils.getTime(circles.get(i).getCreateAt());
            circles.get(i).setCreateAt(time);
        }

        return circles;
    }


    @Override
    public List<CircleClassificationVo> queryCircleRecommendationData(int userId,Paging paging) {
        Integer pages=(paging.getPage()-1)*paging.getLimit();
        String pagings=" limit "+pages+","+paging.getLimit()+"";

        List<CircleClassificationVo> circleLabelVos = circleMapper.queryAllCircles(pagings);

        //得到用户id不等于userId的帖子数据
        List<CircleClassificationVo> circles = circleLabelVos.stream().filter(u -> u.getUId() != userId).collect(Collectors.toList());
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

            //将时间戳转换为多少天或者多少个小时和多少年
            String time = DateUtils.getTime(circles.get(i).getCreateAt());
            circles.get(i).setCreateAt(time);
        }
        Collections.shuffle(circles);

        return circles;
    }

    @Override
    public int addCirclePost(Circle circle) {
        circle.setCreateAt(System.currentTimeMillis()/1000+"");

        int i1 = circleMapper.addCirclePost(circle);
        if(i1<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加圈子帖子失败");
        }

        if(circle.getType()==0) {
            //添加图片组
            Img img = new Img();
            img.setType(1);
            img.setZId(circle.getId());
            img.setCreateAt(System.currentTimeMillis() / 1000 + "");
            for (int i = 0; i < circle.getImg().length; i++) {
                img.setImgUrl(circle.getImg()[i]);
                int addImg = circleMapper.addImg(img);
                if (addImg <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "添加图片组失败");
                }
            }
        }


        return 1;
    }



    @Override
    public Integer deletes(Integer[] id) {
        Integer deletes=0;
        for (int i=0; i<id.length;i++){
            deletes= circleMapper.deletes(id[i]);
        }

        if(deletes<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"批量删除失败");
        }
        return deletes;
    }

    @Override
    public List<CircleClassificationVo> selectPostsBasedTagIdCircle(int id, Paging paging,int userId) {
        Integer pages=(paging.getPage()-1)*paging.getLimit();
        String pagings=" limit "+pages+","+paging.getLimit()+"";

        //根据标签id查询这个标签下面的所有帖子
        List<CircleClassificationVo> circles = circleMapper.selectPostsBasedTagIdCircle(id, pagings);
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

            //将时间戳转换为多少天或者多少个小时和多少年
            String time = DateUtils.getTime(circles.get(i).getCreateAt());
            circles.get(i).setCreateAt(time);
        }

             return circles;
    }

    @Override
    public int givePost(int id, int userId) {
        Give give = circleGiveMapper.selectCountWhether(userId, id);
        if(give==null){
            int i = circleGiveMapper.givePost(id, userId, System.currentTimeMillis() / 1000 + "");
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return i;
        }

        int i =0;
        //如果当前状态是1 那就改为0 取消收藏
        if(give.getGiveCancel()==1){
            i=circleGiveMapper.updateGiveStatus(give.getId(), 0);
        }

        //如果当前状态是0 那就改为1 为收藏状态
        if(give.getGiveCancel()==0){
            i = circleGiveMapper.updateGiveStatus(give.getId(), 1);
        }

        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }


        return i;
    }

    @Override
    public CircleClassificationVo querySingleCircle(int id, int userId) throws ParseException {
        //查询单个圈子
        CircleClassificationVo circleClassificationVo = circleMapper.querySingleCircle(id);
        if(circleClassificationVo==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该圈子的帖子不圈子");
        }
        //在用户登录的情况下 增加帖子浏览记录
        if(userId!=0){
            //得到上一次观看帖子的时间
            Browse browse = new Browse();
            String s = browseMapper.selectCreateAt(id, userId);
            if(s==null){
                //增加浏览记录
                browse.setCreateAt(System.currentTimeMillis()/1000+"");
                browse.setUId(userId);
                browse.setZqId(id);
                browse.setType(1);
                //增加浏览记录
                int i = browseMapper.addBrowse(browse);
                if(i<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"增加浏览记录错误");
                }

                //修改帖子浏览数量
                int i1 = circleMapper.updateBrowse(id);
                if(i1<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
            }else{
                //得到过去时间和现在的时间是否相隔1440分钟 如果相隔了 就添加新的浏览记录
                long minutesApart = TimeUtil.getMinutesApart(s);
                if(minutesApart>=1440){
                    //增加浏览记录
                    browse.setCreateAt(System.currentTimeMillis()/1000+"");
                    browse.setUId(userId);
                    browse.setZqId(id);
                    browse.setType(1);
                    //增加浏览记录
                    int i = browseMapper.addBrowse(browse);
                    if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR,"增加浏览记录错误");
                    }

                    //修改帖子浏览数量
                    int i1 = homeMapper.updateBrowse(id);
                    if(i1<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }

                }
            }

        }


        //得到图片组
        String[] strings = homeMapper.selectImgByPostId(circleClassificationVo.getId());
        circleClassificationVo.setImg(strings);

        //得到点过赞人的头像
        String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circleClassificationVo.getId());
        circleClassificationVo.setGiveAvatar(strings1);

        //得到点赞数量
        Integer integer1 = circleGiveMapper.selectGiveNumber(circleClassificationVo.getId());
        circleClassificationVo.setGiveNumber(integer1);


        //等于0在用户没有到登录的情况下 直接设置没有点赞
        if(userId==0){
            circleClassificationVo.setWhetherGive(0);
        }else{
            //查看我是否关注了此人
            int i1 = attentionMapper.queryWhetherAttention(userId, circleClassificationVo.getUId());
            if(i1>0){
                circleClassificationVo.setWhetherAttention(1);
            }

            //查询是否对帖子点了赞   0没有 1有
            Integer integer = circleGiveMapper.whetherGive(userId, circleClassificationVo.getId());
            if(integer==0){
                circleClassificationVo.setWhetherGive(0);
            }else{
                circleClassificationVo.setWhetherGive(1);
            }
        }


        //得到帖子评论数量
        Integer integer2 = commentMapper.selectCommentNumber(circleClassificationVo.getId());
        circleClassificationVo.setNumberPosts(integer2);

        //得到评论数据
        List<CommentUserVo> comments = commentMapper.selectComment(circleClassificationVo.getId());
        circleClassificationVo.setComments(comments);

        //将时间戳转换为多少天或者多少个小时和多少年
        String time = DateUtils.getTime(circleClassificationVo.getCreateAt());
        circleClassificationVo.setCreateAt(time);


        return circleClassificationVo;
    }

    @Override
    public void issueResourceOrCircle(Circle circle, String imgUrl, int postType, int whetherCover) throws Exception {
        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(circle.getContent(), token);
        if(identifyTextContent.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        //资源帖子
        if(postType==0){
            issue(circle,imgUrl,postType,whetherCover);
        }

        //圈子帖子
        if(postType==1){
            issue(circle,imgUrl,postType,whetherCover);
        }
    }

    @Override
    public List<CircleClassificationVo> selectPostsByCommunityCategoryId(int id,int userId, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pagings="limit "+page+","+paging.getLimit()+"";

        List<CircleClassificationVo> circles = circleMapper.selectPostsBasedTagIdCircleTwo(id, pagings);
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

            //将时间戳转换为多少天或者多少个小时和多少年
            String time = DateUtils.getTime(circles.get(i).getCreateAt());
            circles.get(i).setCreateAt(time);
        }
        return circles;
    }

    @Override
    public CommunityVo selectCommunityCategoryId(int id,int userId) {
        //查询圈子信息
        CommunityVo communityVo = circleMapper.selectCommunityCategoryId(id);
        if(communityVo==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"没有该圈子");
        }
        //得到圈子总人数
        int i = communityMapper.selectTotalNumberCirclesById(communityVo.getId());
        communityVo.setTotalNumberCircles(i);

        //查询圈子的用户头像
        String[] strings = communityMapper.selectCirclesAvatars(communityVo.getId());
        communityVo.setAvatar(strings);


        List<CommunityUser> communities = communityMapper.queryCommunityById(communityVo.getId());
        //匹配是否存在这个圈子
        communities.stream().filter(u->u.getUserId()==userId).forEach(u->{
                communityVo.setWhetherThere(1);
        });

        //得到单元体导航栏
        List<Haplont> haplonts = haplontMapper.selectHaplontByTagId(id);
        communityVo.setHaplontList(haplonts);
        return communityVo;
    }

    @Override
    public int joinCircle(CommunityUser communityUser) {
        communityUser.setCreateAt(System.currentTimeMillis()/1000+"");

        //查询是否存在圈子里面 如果存在在调用接口就是退出圈子
        int i1 = communityMapper.queryWhetherThereCircle(communityUser.getCommunityId(), communityUser.getUserId());
        if(i1>0){
            //退出圈子
            int i = communityMapper.exitGroupChat(communityUser.getCommunityId(), communityUser.getUserId());
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"退出圈子失败");
            }
            return i;
        }

        int i = circleMapper.joinCircle(communityUser);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"加入圈子失败!");
        }
        return i;
    }

    @Override
    public void internalRelease(Circle circle, String imgUrl, int postType, int whetherCover) throws Exception {
        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(circle.getContent(), token);
        if(identifyTextContent.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }


        if(circle.getHaplontType()==100) {
            circle.setHaplontType(0);
        }



        //根据二级标签查询一级标签
        int tagsOne = tagMapper.queryLabelAccordingSecondaryId(circle.getTagsTwo());
        circle.setTagsOne(tagsOne);




        //资源帖子
        if(postType==0){
            //获取token
            String token1 = ConstantUtil.getToken();
            String identifyTextContent1 = ConstantUtil.identifyText(circle.getTitle(), token1);
            if(identifyTextContent1.equals("87014")){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
            }

            issue(circle,imgUrl,postType,whetherCover);
        }

        //圈子帖子
        if(postType==1){
            //10是提问 添加到提问表
            /*if(circle.getHaplontType()==10){
                question.setDescription(circle.getContent());
                question.setTagsOne(tagsOne);
                question.setTagsTwo(circle.getTagsTwo());
                //添加提问信息
                int i = questionMapper.addQuestion(question);
                if(i<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"添加提问信息失败");
                }
                return i;
            }*/

            issue(circle,imgUrl,postType,whetherCover);

        }


    }

    @Override
    public List<Tag> queryHowManyPostsAreInEachCell(int id) {
        //根据一级标签查询所有二级标签下面的帖子数量
        List<Tag> tags = circleMapper.queryHowManyPostsAreInEachCell(id);

        //根据数量排序再取前5条数据
        List<Tag> collect = tags.stream().sorted(Comparator.comparing(Tag::getNum).reversed()).limit(5).collect(Collectors.toList());

        return collect;
    }

    public void issue(Circle circle, String imgUrl, int postType, int whetherCover)throws Exception{
        circle.setCreateAt(System.currentTimeMillis()/1000+"");

        String[] split = null;

        //自己选封面
        if(whetherCover==1){
            if(circle.getType()==0){
                split=imgUrl.split(",");
            }

        }

        //系统默认封面
        if(whetherCover==0){
            if(circle.getType()==0){
                split=imgUrl.split(",");
                circle.setCover(split[0]);
            }else{
                String videoCover = FfmpegUtil.getVideoCover(circle.getVideo());
                circle.setCover(videoCover);
            }
        }

        //圈子添加
        if(postType==1){
            int i = circleMapper.addCirclePost(circle);
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }

            if(circle.getType()==0){
                int addImg = homeMapper.addImg(circle.getId(), split, System.currentTimeMillis() / 1000 + "", postType);
                if(addImg<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
            }
        }

        //资源添加
        if(postType==0){
            Resources resources=new Resources();

            //实体类的值赋给另一个实体类
            BeanUtils.copyProperties(circle,resources);

            int i = homeMapper.addResourcesPost(resources);
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }

            if(circle.getType()==0){
                int addImg = homeMapper.addImg(resources.getId(), split, System.currentTimeMillis() / 1000 + "", postType);
                if(addImg<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
            }
        }



    }


}
