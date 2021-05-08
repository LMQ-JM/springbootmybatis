package com.example.learn.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.learn.dao.DryGoodsCollectMapper;
import com.example.learn.dao.PublicClassMapper;
import com.example.learn.entity.ClassList;
import com.example.learn.entity.Collect;
import com.example.learn.service.IPublicClassService;
import com.example.learn.vo.PublicClassVo;
import com.example.user.dao.UserMapper;
import com.example.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/6 9:47
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PublicClassServiceImpl implements IPublicClassService {

    @Autowired
    private PublicClassMapper publicClassMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DryGoodsCollectMapper dryGoodsCollectMapper;

    @Override
    public PublicClassVo queryPublicClassById(int id, int userId) {
        PublicClassVo publicClassVo = publicClassMapper.queryPublicClassById(id);
        //获取发帖人名称,头像,介绍
        User user = userMapper.selectUserById(publicClassVo.getUId());
        if(user.getUserName()==null || user.getAvatar()==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        publicClassVo.setUName(user.getUserName());
        publicClassVo.setUAvatar(user.getAvatar());
        publicClassVo.setIntroduce(user.getIntroduce());
        //课程列表
//        List<ClassList> cs = JSONArray.parseArray(publicClassVo.getClassList(), ClassList.class);
//        publicClassVo.setClassLists(cs);
        //如果userId为0，用户处于未登录状态，状态设为未收藏
        if (userId == 0){
            publicClassVo.setWhetherCollect(0);
            return publicClassVo;
        }
        //我是否对该帖子收过藏
        Integer collectStatus = dryGoodsCollectMapper.whetherCollect(2,userId, publicClassVo.getId());
        if (collectStatus == 0) {
            publicClassVo.setWhetherCollect(0);
        } else {
            publicClassVo.setWhetherCollect(1);
        }
        return publicClassVo;
    }

    @Override
    public int giveCollect(int id, int userId) {
        //查询数据库是否存在该条数据
        Collect collect = dryGoodsCollectMapper.selectCountWhether(2,userId,id);
        if(collect == null){
            int i = dryGoodsCollectMapper.giveCollect(2,id, userId, System.currentTimeMillis() / 1000 + "");
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            int j = publicClassMapper.updatePublicClassCollect(id,"+");
            if(j<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return j;
        }
        int i = 0;
        int j = 0;
        //如果当前状态是1 那就改为0 取消收藏
        if(collect.getGiveCancel()==1){
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 0);
            j = publicClassMapper.updatePublicClassCollect(id,"-");
        }
        //如果当前状态是0 那就改为1 为收藏状态
        if(collect.getGiveCancel()==0){
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 1);
            j = publicClassMapper.updatePublicClassCollect(id,"+");
        }
        if(i<=0 || j<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return j;
    }
}
