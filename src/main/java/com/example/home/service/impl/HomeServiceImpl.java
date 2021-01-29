package com.example.home.service.impl;

import com.example.circle.entity.Circle;
import com.example.common.utils.Paging;
import com.example.home.dao.HomeMapper;
import com.example.home.entity.Resources;
import com.example.home.service.IHomeService;
import com.example.tags.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 */
@Service
public class HomeServiceImpl implements IHomeService {

    @Autowired
    private HomeMapper homeMapper;

    @Override
    public List<Circle> selectAllSearch(String postingName, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";
        List<Circle> circles = homeMapper.selectAllSearch(postingName, sql);
        return circles;
    }

    @Override
    public List<Tag> selectFirstLevelLabelResource() {
        return homeMapper.selectFirstLevelLabelResource();
    }

    @Override
    public List<Tag> selectSecondaryLabel(int id) {
        return homeMapper.selectSecondaryLabel(id);
    }

    @Override
    public <T>T selectPostingOrLabel(int id,Paging paging) {
        //查询第三级标签 如果没有 就根据二级标签查询出帖子数据
        List<Tag> tags = homeMapper.selectPostingOrLabel(id);
        if(tags.size()==0 || tags==null){
            Integer page=(paging.getPage()-1)*paging.getLimit();
            String pagings="limit "+page+","+paging.getLimit()+"";
            List<Resources> resources = homeMapper.selectPosting(id, pagings);
            return (T)resources;
        }

        return (T)tags;
    }

    @Override
    public List<Resources> selectPostingByThreeLabelId(int id, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pagings="limit "+page+","+paging.getLimit()+"";

        List<Resources> resources = homeMapper.selectPostingByThreeLabelId(id, paging);
        return resources;
    }

    @Override
    public Resources selectSingleResourcePost(int id) {
        Resources resources = homeMapper.selectSingleResourcePost(id);
        return resources;
    }


}
