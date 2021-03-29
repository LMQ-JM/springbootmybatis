package com.example.home.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ConstantUtil;
import com.example.home.dao.CompanyMapper;
import com.example.home.entity.RecruitingUsers;
import com.example.home.service.ICompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

/**
 * @author MQ
 * @date 2021/3/29 15:20
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, RecruitingUsers> implements ICompanyService {

    @Autowired
    private CompanyMapper companyMapper;


    @Override
    public int addRecruitingUsers(RecruitingUsers recruitingUsers) throws ParseException {
        //查询是否注册过公司信息
        int i = companyMapper.queryCount(recruitingUsers.getUserId());
        if(i>0){
            //修改公司信息
            QueryWrapper queryWrapper=new QueryWrapper();
            queryWrapper.eq("user_id",recruitingUsers.getUserId());

            int update = baseMapper.update(recruitingUsers, queryWrapper);
            if(update<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"修改失败");
            }
            return update;
        }


        //获取token
        String token = ConstantUtil.getToken();

        //验证公司名称内容是否有误
        String identifyTextContent = ConstantUtil.identifyText(recruitingUsers.getCorporateName(), token);
        if(identifyTextContent.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        //验证公司描述内容是否有误
        String identifyTextContent1 = ConstantUtil.identifyText(recruitingUsers.getCompanyDescription(), token);
        if(identifyTextContent1.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        recruitingUsers.setCreateAt(System.currentTimeMillis()/1000+"");

        //添加
        int insert = baseMapper.insert(recruitingUsers);
        if(insert<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加公司信息失败");
        }

        return insert;
    }

    @Override
    public int queryCount(int userId) {
        return companyMapper.queryCount(userId);
    }



    @Override
    public RecruitingUsers querySingleCompanyInformation(int userId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        RecruitingUsers recruitingUsers = baseMapper.selectOne(queryWrapper);
        return recruitingUsers;
    }
}
