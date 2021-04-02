package com.example.home.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ConstantUtil;
import com.example.common.utils.Paging;
import com.example.home.dao.RecruitJobSearchMapper;
import com.example.home.entity.RecruitJobSearch;
import com.example.home.entity.RecruitLabel;
import com.example.home.service.IRecruitJobSearchService;
import com.example.home.vo.RecruitJobSearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/3/30 16:07
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RecruitJobSearchServiceImpl extends ServiceImpl<RecruitJobSearchMapper, RecruitJobSearch> implements IRecruitJobSearchService {

    @Autowired
    private RecruitJobSearchMapper recruitJobSearchMapper;


    @Override
    public List<RecruitJobSearchVo> queryJobInformation(int typeId, Paging paging, int orderBy) {
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

        //(0找全职，1找兼职)
        //查询找全职
        List<RecruitJobSearchVo> recruitJobSearchVos = recruitJobSearchMapper.queryJobInformation(typeId, pag, orderBys);
        for (int i=0;i<recruitJobSearchVos.size();i++){
            ////根据id得到这个文章的标签
            List<RecruitLabel> recruitLabels = recruitJobSearchMapper.queryRecruitJobSearchLabelById(recruitJobSearchVos.get(i).getId());
            recruitJobSearchVos.get(i).setRecruitLabels(recruitLabels);
        }

        return recruitJobSearchVos;

    }

    @Override
    public void addJobHunting(RecruitJobSearch recruitJobSearch,Integer[] label) throws ParseException {

        //获取token
        String token = ConstantUtil.getToken();

        //职位名称
        String identifyTextContent = ConstantUtil.identifyText(recruitJobSearch.getJobTitle(), token);
        if(identifyTextContent.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        //内容
        String identifyTextContent1 = ConstantUtil.identifyText(recruitJobSearch.getJobContent(), token);
        if(identifyTextContent1.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        recruitJobSearch.setCreateAt(System.currentTimeMillis()/1000+"");

        //添加发布的岗位信息
        int insert = baseMapper.insert(recruitJobSearch);
        if(insert<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加失败");
        }

        //添加标签组数据
        int i = recruitJobSearchMapper.addRecruitJobSearchLabelMiddle(recruitJobSearch.getId(), label);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加标签组错误");
        }




    }

    @Override
    public RecruitJobSearchVo queryJobSearchDetails(int id) {

        //根据id查询详情
        RecruitJobSearchVo recruitJobSearchVo = recruitJobSearchMapper.queryJobSearchDetails(id);

        //根据id得到这个文章的标签
        List<RecruitLabel> recruitLabels = recruitJobSearchMapper.queryRecruitJobSearchLabelById(recruitJobSearchVo.getId());
        recruitJobSearchVo.setRecruitLabels(recruitLabels);

        return recruitJobSearchVo;
    }
}
