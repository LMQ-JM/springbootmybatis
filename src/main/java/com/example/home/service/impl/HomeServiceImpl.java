package com.example.home.service.impl;

import com.example.circle.entity.Circle;
import com.example.common.utils.Paging;
import com.example.common.utils.ReturnVo;
import com.example.home.dao.HomeMapper;
import com.example.home.entity.Resources;
import com.example.home.service.IHomeService;
import com.example.home.vo.HomeClassificationVo;
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
    public List<HomeClassificationVo> selectResourceLearningExchange(int id, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pagings="limit "+page+","+paging.getLimit()+"";
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
    public List<Resources> selectPostsByCommunityCategoryIds(int id, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pagings="limit "+page+","+paging.getLimit()+"";
        List<Resources> resources = homeMapper.selectPostsByCommunityCategoryIds(id, pagings);
        for (int i=0;i<resources.size();i++){
            //根据帖子id查询出当前帖子图片
            String[] strings = homeMapper.selectImgByPostId(resources.get(i).getId());
            resources.get(i).setImg(strings);
        }
        return resources;
    }


    @Override
    public List<Tag> selectSecondaryLabel(int id) {
        return homeMapper.selectSecondaryLabel(id);
    }

    @Override
    public List<Resources> selectAllPostsSecondaryTagId(int id, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pagings="limit "+page+","+paging.getLimit()+"";
        List<Resources> resources = homeMapper.selectAllPostsSecondaryTagId(id, pagings);
        for (int i=0;i<resources.size();i++){
            //根据帖子id查询出当前帖子图片
            String[] strings = homeMapper.selectImgByPostId(resources.get(i).getId());
            resources.get(i).setImg(strings);
        }
        return resources;
    }








    @Override
    public ReturnVo selectPostingOrLabel(int id, Paging paging) {
        //查询第三级标签 如果没有 就根据二级标签查询出帖子数据
        List<Tag> tags = homeMapper.selectPostingOrLabel(id);
        ReturnVo returnVo=new ReturnVo();

        if(tags.size()==0 || tags==null){
            Integer page=(paging.getPage()-1)*paging.getLimit();
            String pagings="limit "+page+","+paging.getLimit()+"";
            List<Resources> resources = homeMapper.selectPosting(id, pagings);
            for (int i=0;i<resources.size();i++){
                //根据帖子id查询出当前帖子图片
                String[] strings = homeMapper.selectImgByPostId(resources.get(i).getId());
                resources.get(i).setImg(strings);
            }

            returnVo.setList(resources);
            returnVo.setCount(1);
        }

        returnVo.setList(tags);
        returnVo.setCount(2);
        return returnVo;
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
