package com.example.circle.service.impl;

import com.example.circle.dao.AttentionMapper;
import com.example.circle.entity.Attention;
import com.example.circle.service.IAttentionService;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public int addAttention(Attention attention) {
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
    public User queryPeopleFollow(int userId) {
        return attentionMapper.queryPeopleFollow(userId);
    }

    @Override
    public User queryRecommendedUserData(int userId) {


        return null;
    }
}
