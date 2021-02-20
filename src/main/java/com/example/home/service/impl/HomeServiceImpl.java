package com.example.home.service.impl;

import com.example.circle.entity.Circle;
import com.example.common.utils.Paging;
import com.example.home.dao.HomeMapper;
import com.example.home.dao.RecruitMapper;
import com.example.home.entity.Recruit;
import com.example.home.entity.RecruitLabel;
import com.example.home.entity.Resources;
import com.example.home.service.IHomeService;
import com.example.home.vo.HomeClassificationVo;
import com.example.home.vo.RecruitVo;
import com.example.tags.entity.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Administrator
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class HomeServiceImpl implements IHomeService {

    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private RecruitMapper recruitMapper;

    @Override
    public List<Circle> selectAllSearch(String postingName, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";
        return homeMapper.selectAllSearch(postingName, sql);
    }

    @Override
    public List<Tag> selectFirstLevelLabelResource() {
        return homeMapper.selectFirstLevelLabelResource();
    }

    @Override
    public Object selectResourceLearningExchange(int id, Paging paging) {

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pagings="limit "+page+","+paging.getLimit()+"";

        //点击人才 进入判断 返回人才页面格式的数据
        if(id==14){
            List<RecruitVo> recruits = recruitMapper.selectAllRecruit(pagings,"ORDER BY a.view_count desc");
            for (int i=0;i<recruits.size();i++){
                //得到岗位要求标签组
                List<RecruitLabel> recruitLabels = recruitMapper.selectRecruitLabelById(recruits.get(i).getId());
                recruits.get(i).setRecruitLabels(recruitLabels);
            }
            return recruits;
        }

        List<HomeClassificationVo> homeClassificationVos = homeMapper.selectResourceLearningExchange(id, pagings);
        for (int i=0;i<homeClassificationVos.size();i++){
            //根据帖子id查询出当前帖子图片
            String[] strings = homeMapper.selectImgByPostId(homeClassificationVos.get(i).getId());

            homeClassificationVos.get(i).setImg(strings);
        }
        return homeClassificationVos;
    }

    @Override
    public List<Resources> selectPostsByCommunityCategoryId(int id, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pagings="limit "+page+","+paging.getLimit()+"";
        List<Resources> resources = homeMapper.selectPostsByCommunityCategoryId(id, pagings);
        for (int i=0;i<resources.size();i++){
            //根据帖子id查询出当前帖子图片
            String[] strings = homeMapper.selectImgByPostId(resources.get(i).getId());
            resources.get(i).setImg(strings);
        }
        return resources;
    }



    @Override
    public Resources selectSingleResourcePost(int id) {
        return homeMapper.selectSingleResourcePost(id);
    }


}
