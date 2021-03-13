package com.example.home.service.impl;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.Paging;
import com.example.home.dao.JobWantedMapper;
import com.example.home.dao.RecruitMapper;
import com.example.home.entity.JobWanted;
import com.example.home.entity.RecruitLabel;
import com.example.home.service.IRecruitService;
import com.example.home.vo.RecruitVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MQ
 * @create 2021/2/20
 **/
@Service
public class RecruitServiceImpl implements IRecruitService {

    @Autowired
    private RecruitMapper recruitMapper;

    @Autowired
    private JobWantedMapper jobWantedMapper;


    @Override
    public List<RecruitVo> selectSignboardInformation(int typeId, Paging paging,int userId,int orderBy) {
        String orderBys="";

        //最热
        if(orderBy==0){
            orderBys="ORDER BY a.view_count desc";
        }

        //最新
        if(orderBy==1){
            orderBys="ORDER BY a.create_at desc";
        }

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pag="limit "+page+","+paging.getLimit()+"";
        //查询全部
        if(typeId==0){
            List<RecruitVo> recruitVos = recruitMapper.selectAllRecruit(pag,orderBys);
            for (int i=0;i<recruitVos.size();i++){
                //得到岗位要求标签组
                List<RecruitLabel> recruitLabels = recruitMapper.selectRecruitLabelById(recruitVos.get(i).getId());
                recruitVos.get(i).setRecruitLabels(recruitLabels);
            }
            return recruitVos;
        }

        //根据用户id查询自己发布的招聘信息
        if(userId!=0){
            List<RecruitVo> recruitVos = recruitMapper.selectSignboardInformationByUserId(userId, pag);
            for (int i=0;i<recruitVos.size();i++){
                //得到岗位要求标签组
                List<RecruitLabel> recruitLabels = recruitMapper.selectRecruitLabelById(recruitVos.get(i).getId());
                recruitVos.get(i).setRecruitLabels(recruitLabels);
            }
            return recruitVos;
        }


        List<RecruitVo> recruitVos = recruitMapper.selectSignboardInformation(typeId, pag,orderBys);
        for (int i=0;i<recruitVos.size();i++){
            //得到岗位要求标签组
            List<RecruitLabel> recruitLabels = recruitMapper.selectRecruitLabelById(recruitVos.get(i).getId());
            recruitVos.get(i).setRecruitLabels(recruitLabels);
        }
        return recruitVos;
    }

    @Override
    public int increasePageViews(int id) {
        int i = recruitMapper.increasePageViews(id);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return i;
    }

    @Override
    public RecruitVo selectViewDetails(int id) {
        RecruitVo recruitVos = recruitMapper.selectViewDetails(id);
        //得到岗位要求标签组
        List<RecruitLabel> recruitLabels = recruitMapper.selectRecruitLabelById(recruitVos.getId());
        recruitVos.setRecruitLabels(recruitLabels);
        if(recruitVos==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return recruitVos;
    }

    @Override
    public List<RecruitVo> selectPostingBasedUserID(int userId, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pag="limit "+page+","+paging.getLimit()+"";
        return recruitMapper.selectSignboardInformationByUserId(userId,pag);
    }

    @Override
    public int addJobExpectations(JobWanted jobWanted) {
        jobWanted.setCreateAt(System.currentTimeMillis()/1000+"");
        int i = jobWantedMapper.addJobExpectations(jobWanted);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加求职期望失败！");
        }
        return i;
    }
}
