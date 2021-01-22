package com.example.posting.service.impl;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ReturnVo;
import com.example.posting.dao.PostingMapper;
import com.example.posting.service.IPostingService;
import com.example.posting.vo.PostingReturnVo;
import com.example.posting.vo.PostingVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author MQ
 * @date 2021/1/15 10:19
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PostingServiceImpl implements IPostingService {

    @Autowired
    private PostingMapper postingMapper;

    @Override
    public ReturnVo selectAllPosting(PostingVo postingVo) throws ParseException {
        String sql="";

        Integer page=(postingVo.getPage()-1)*postingVo.getLimit();
        if(!postingVo.getPostingname().equals("undefined") && !postingVo.getPostingname().equals("")){
            sql+=" and a.name like '%"+postingVo.getPostingname()+"%'";
        }
        //如果发帖人不为空 ，根据发帖人查询帖子
        if(!postingVo.getUserName().equals("undefined") && !postingVo.getUserName().equals("")){
            sql+="and b.name like '%"+postingVo.getUserName()+"%'";
        }

        //将时间格式转换为时间戳
        //开始时间
        if(!postingVo.getStartTime().equals("undefined") && !postingVo.getEndTime().equals("undefined") ){
            if(!postingVo.getStartTime().equals("") && !postingVo.getEndTime().equals("")){

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String startTime=String.valueOf(sdf.parse(postingVo.getStartTime()).getTime() / 1000);

                //结束时间
                SimpleDateFormat sdftwo = new SimpleDateFormat("yyyy-MM-dd");
                String endTime=String.valueOf(sdftwo.parse(postingVo.getEndTime()).getTime() / 1000);

                if(!postingVo.getStartTime().equals("undefined") && !postingVo.getEndTime().equals("undefined")){
                    sql+="and a.create_at>= "+startTime+" and a.create_at<="+endTime+"";
                }
            }
        }
        String paging=" limit "+page+","+postingVo.getLimit()+"";
        List<PostingReturnVo> postingReturnVos = postingMapper.selectAllPosting(sql, paging);

        //根据不同条件得到不同帖子数量
        Integer integer = postingMapper.selectAllPostingCount(sql);

        ReturnVo returnVo=new ReturnVo();
        returnVo.setCount(integer);
        returnVo.setList(postingReturnVos);

        return returnVo;
    }

    @Override
    public Integer deletes(Integer[] id) {
        Integer deletes=0;
        for (int i=0; i<id.length;i++){
            deletes= postingMapper.deletes(id[i]);
        }

        if(deletes<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"批量删除失败");
        }
        return deletes;
    }
}
