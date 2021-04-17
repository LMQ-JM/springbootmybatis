package com.example.tags.service.impl;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ConstantUtil;
import com.example.home.entity.Community;
import com.example.tags.dao.TagMapper;
import com.example.tags.entity.Tag;
import com.example.tags.service.ITagService;
import com.example.tags.vo.AllTagVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/1/21 16:08
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TagServiceImpl implements ITagService {
    @Autowired
    private TagMapper tagMapper;



    @Override
    public List<Tag> selectResourcesAllTag(int type) {
        List<Tag> tags = tagMapper.selectResourcesAllTag(type);
        for (int i=0;i<tags.size();i++){
            if("人才".equals(tags.get(i).getTagName())){
                tags.remove(i);
            }
        }
        return tags;
    }

    @Override
    public List<Tag> selectResourcesAllTags(int tid) {
        //根据一级标签查询二级标签数据
        List<Tag> tags = tagMapper.selectResourcesAllTags(tid);

        String str="";

        if(tags.get(0).getType()==0){
            str="tb_resources";
        }

        if(tags.get(0).getType()==1){
            str="tb_circles";
        }
       /* //将对象List中的某个字段放到新的List中
        List<Integer> stringList = tags.stream().map(Tag::getId).collect(Collectors.toList());*/

       for (int i=0;i<tags.size();i++){
           //根据二级标签组查询每个标签发过多少个帖子
           int i1 = tagMapper.selectTagsNum(tags.get(i).getId(), str);
           tags.get(i).setNum(i1);
       }

        return tags;
    }

    @Override
    public List<AllTagVo> queryAllPrimaryAndSecondaryTags() {
        String str="";

        List<AllTagVo> tags = tagMapper.queryAllTag();
        for (int i=0;i<tags.size();i++){
            //根据一级标签查询所有二级标签下面的帖子数量
            if(tags.get(i).getType()==0){
                str="tb_resources";
            }

            if(tags.get(i).getType()==1){
                str="tb_circles";
            }
            //查询每个标签 下面有多少个帖子
            List<Tag> tags1 = tagMapper.queryHowManyPostsAreInEachCell(str, tags.get(i).getId());
            tags.get(i).setTag(tags1);
        }

        return tags;
    }

    @Override
    public void addTag(Tag tag, Community community) throws ParseException {

        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(tag.getTagName(), token);
        if(identifyTextContent.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        //获取token
        String token1 = ConstantUtil.getToken();
        String identifyTextContent1 = ConstantUtil.identifyText(community.getIntroduce(), token1);
        if(identifyTextContent1.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        //获取token
        String token2 = ConstantUtil.getToken();
        String identifyTextContent2 = ConstantUtil.identifyText(community.getAnnouncement(), token2);
        if(identifyTextContent2.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        tag.setCreateAt(System.currentTimeMillis()/1000+"");
        community.setCreateAt(System.currentTimeMillis()/1000+"");

        community.setPosters(tag.getImgUrl());
        community.setCommunityName(tag.getTagName());

        tag.setType(1);
        community.setType(1);

        int i = tagMapper.addTag(tag);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加失败");
        }

        community.setTagId(tag.getId());
        int i1 = tagMapper.addCommunity(community);
        if(i1<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加圈子信息失败");
        }

        int i2 = tagMapper.addTagHaplont(tag.getId(), 1);
        if(i2<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        int i3 = tagMapper.addTagHaplont(tag.getId(), 2);
        if(i3<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
    }


}
