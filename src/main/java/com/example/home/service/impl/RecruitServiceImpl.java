package com.example.home.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ConstantUtil;
import com.example.common.utils.Paging;
import com.example.home.dao.CompanyMapper;
import com.example.home.dao.RecruitMapper;
import com.example.home.entity.Recruit;
import com.example.home.entity.RecruitLabel;
import com.example.home.service.IRecruitService;
import com.example.home.vo.RecruitVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @create 2021/2/20
 **/
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class RecruitServiceImpl extends ServiceImpl<RecruitMapper,Recruit> implements IRecruitService {

    @Autowired
    private RecruitMapper recruitMapper;

    @Autowired
    private CompanyMapper companyMapper;

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
        //点击我的
        if(typeId==1){
            if(userId!=0){
                List<RecruitVo> recruitVos = recruitMapper.selectSignboardInformationByUserId(userId, pag);
                for (int i=0;i<recruitVos.size();i++){
                    //得到岗位要求标签组
                    List<RecruitLabel> recruitLabels = recruitMapper.selectRecruitLabelById(recruitVos.get(i).getId());
                    recruitVos.get(i).setRecruitLabels(recruitLabels);
                }
                return recruitVos;
            }
        }

        //(0全部，1我的，2招全职，3招兼职，4找全职，5找兼职)
        List<RecruitVo> recruitVos=null;
        if(typeId>1){
            //根据状态查询人才招聘信息
            recruitVos = recruitMapper.selectSignboardInformation(typeId, pag,orderBys);
            for (int i=0;i<recruitVos.size();i++){
                //得到岗位要求标签组
                List<RecruitLabel> recruitLabels = recruitMapper.selectRecruitLabelById(recruitVos.get(i).getId());
                recruitVos.get(i).setRecruitLabels(recruitLabels);
            }
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
    public int addJobExpectations(Recruit recruit,Integer[] label) throws ParseException {
        //查询是否注册过公司
        int i1 = companyMapper.queryCount(recruit.getCompanyId());
        if(i1<=0){
            return 404;
        }

        //获取token
        String token = ConstantUtil.getToken();

        //职位名称
        String identifyTextContent = ConstantUtil.identifyText(recruit.getJobTitle(), token);
        if(identifyTextContent.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        //职位要求
        String identifyTextContent2 = ConstantUtil.identifyText(recruit.getJobRequirements(), token);
        if(identifyTextContent2.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }


        recruit.setCreateAt(System.currentTimeMillis()/1000+"");
        recruit.setIsDelete(1);
        //添加发布的职位
        int insert = baseMapper.insert(recruit);
        if(insert<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加发布的职位失败");
        }

        if(recruit.getId().equals("")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"id没拿到");
        }

        /**
         * 添加职位和标签的中间表数据
         */
        int i = recruitMapper.addRecruitLabelMiddle(recruit.getId(), label);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加职位和标签的中间表数据失败");
        }

        return i;
    }

    @Override
    public List<RecruitLabel> queryCheckJobRequirementsTab() {
        List<RecruitLabel> recruitLabels = recruitMapper.queryCheckJobRequirementsTab();
        return recruitLabels;
    }



}
