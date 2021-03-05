package com.example.circle.service.impl;

import com.example.circle.dao.CircleGiveMapper;
import com.example.circle.dao.CircleMapper;
import com.example.circle.dao.CommentMapper;
import com.example.circle.entity.Circle;
import com.example.circle.entity.Give;
import com.example.circle.entity.Img;
import com.example.circle.service.ICircleService;
import com.example.circle.vo.CircleClassificationVo;
import com.example.circle.vo.CircleLabelVo;
import com.example.circle.vo.CommentUserVo;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.Paging;
import com.example.common.utils.ReturnVo;
import com.example.home.dao.HomeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

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

    @Override
    public ReturnVo queryAllCircles() {
        List<CircleLabelVo> circles = circleMapper.queryAllCircles();
        Integer integer = circleMapper.countAllCircles();

        ReturnVo returnVo=new ReturnVo();
        returnVo.setList(circles);
        returnVo.setCount(integer);
        return returnVo;
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
    public ReturnVo selectAllPosting(Circle circle,Integer page,Integer limit,String startTime,String endTime) throws ParseException {
        String sql="";
        Integer pages=(page-1)*limit;
        if(!circle.getTitle().equals("undefined") && !circle.getTitle().equals("")){
            sql+=" and a.title like '%"+circle.getTitle()+"%'";
        }
        //如果发帖人不为空 ，根据发帖人查询帖子
        if(!circle.getUserName().equals("undefined") && !circle.getUserName().equals("")){
            sql+="and a.user_name like '%"+circle.getUserName()+"%'";
        }

        //将时间格式转换为时间戳
        //开始时间
        if(!startTime.equals("undefined") && !endTime.equals("undefined") && !startTime.equals("null") && !endTime.equals("null")){
            if(!startTime.equals("") && !endTime.equals("")){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String startTimes=String.valueOf(sdf.parse(startTime).getTime() / 1000);

                //结束时间
                SimpleDateFormat sdftwo = new SimpleDateFormat("yyyy-MM-dd");
                String endTimes=String.valueOf(sdftwo.parse(endTime).getTime() / 1000);

                if(!"undefined".equals(startTime) && !endTime.equals("undefined") ){
                    sql+="and a.create_at>= "+startTimes+" and a.create_at<="+endTimes+"";
                }
            }
        }
        String paging=" limit "+pages+","+limit+"";
        List<CircleLabelVo> circleLabelVos = circleMapper.selectAllPosting(sql, paging);
        //根据不同条件得到不同帖子数量
        Integer integer = circleMapper.selectAllPostingCount(sql);

        ReturnVo returnVo=new ReturnVo();
        returnVo.setCount(integer);
        returnVo.setList(circleLabelVos);

        return returnVo;
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
            }else{
                //查询是否对帖子点了赞   0没有 1有
                Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                if(integer==0){
                    circles.get(i).setWhetherGive(0);
                }else{
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
    public CircleClassificationVo querySingleCircle(int id, int userId) {
        CircleClassificationVo circleClassificationVo = circleMapper.querySingleCircle(id);
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


        return circleClassificationVo;
    }


}
