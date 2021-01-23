package com.example.circle.service.impl;

import com.example.circle.dao.CircleMapper;
import com.example.circle.entity.Circle;
import com.example.circle.entity.Img;
import com.example.circle.service.ICircleService;
import com.example.circle.vo.CircleLabelVo;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ReturnVo;
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
            img.setType(circle.getType());
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
}
