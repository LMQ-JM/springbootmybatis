package com.example.home.service.impl;

import com.example.circle.dao.CircleMapper;
import com.example.circle.entity.Circle;
import com.example.circle.entity.Img;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.DateUtils;
import com.example.common.utils.Paging;
import com.example.common.utils.TimeUtil;
import com.example.home.dao.BrowseMapper;
import com.example.home.dao.GiveMapper;
import com.example.home.dao.HomeMapper;
import com.example.home.dao.RecruitMapper;
import com.example.home.entity.Browse;
import com.example.home.entity.Recruit;
import com.example.home.entity.RecruitLabel;
import com.example.home.entity.Resources;
import com.example.home.service.IHomeService;
import com.example.home.vo.HomeClassificationVo;
import com.example.home.vo.RecruitVo;
import com.example.home.vo.ResourcesVo;
import com.example.tags.entity.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
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

    @Autowired
    private BrowseMapper browseMapper;

    @Autowired
    private GiveMapper giveMapper;

    @Autowired
    private CircleMapper circleMapper;

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
    public ResourcesVo selectSingleResourcePost(int id,int userId) throws ParseException {
        ResourcesVo resourcesVo = homeMapper.selectSingleResourcePost(id);

        //得到过去时间和现在的时间是否相隔1440分钟 如果相隔了 就添加新的浏览记录
        long minutesApart = TimeUtil.getMinutesApart(resourcesVo.getCreateAt());
        if(minutesApart>=1440){
            //增加浏览记录
            Browse browse=new Browse();
            browse.setCreateAt(System.currentTimeMillis()/1000+"");
            browse.setUId(userId);
            browse.setZqId(id);
            browse.setType(0);
            //增加浏览记录
            int i = browseMapper.addBrowse(browse);
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"增加浏览记录错误");
            }

            //修改帖子浏览数量
            int i1 = homeMapper.updateBrowse(id,System.currentTimeMillis()/1000+"");
            if(i1<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
        }

        //得到当前时间戳和过去时间戳比较相隔多少分钟或者多少小时或者都少天或者多少年
        String time = DateUtils.getTime(resourcesVo.getCreateAt());
        //根据帖子id查询出当前帖子图片
        String[] strings = homeMapper.selectImgByPostId(resourcesVo.getId());
        resourcesVo.setImg(strings);
        resourcesVo.setCreateAt(String.valueOf(time));


        //得到点过赞人的头像
        String[] strings1 = giveMapper.selectGivePersonAvatar(id);
        resourcesVo.setGiveAvatar(strings1);

        return resourcesVo;
    }

    @Override
    public List<HomeClassificationVo> selectRecommendedSecondaryTagId(int id) {
        List<HomeClassificationVo> homeClassificationVos = homeMapper.selectRecommendedSecondaryTagId(id);
        return homeClassificationVos;
    }

    @Override
    public int addResourcesPost(Resources resources) {
        resources.setCreateAt(System.currentTimeMillis()/1000+"");

        int i1 = homeMapper.addResourcesPost(resources);
        if(i1<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加圈子帖子失败");
        }


        if(resources.getType()==0) {
            //添加图片组
            Img img = new Img();
            img.setType(0);
            img.setZId(resources.getId());
            img.setCreateAt(System.currentTimeMillis() / 1000 + "");
            for (int i = 0; i < resources.getImg().length; i++) {
                img.setImgUrl(resources.getImg()[i]);
                int addImg = circleMapper.addImg(img);
                if (addImg <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "添加图片组失败");
                }
            }
        }


        return 1;
    }


}
