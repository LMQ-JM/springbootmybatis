package com.example.tags.service.impl;

import com.example.tags.dao.TagMapper;
import com.example.tags.entity.Tag;
import com.example.tags.service.ITagService;
import com.example.tags.vo.TagsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<TagsVo> selectResourcesAllTags(int tid) {
        //根据一级标签查询二级标签数据
        List<Tag> tags = tagMapper.selectResourcesAllTags(tid);

        //将对象List中的某个字段放到新的List中
        List<Integer> stringList = tags.stream().map(Tag::getId).collect(Collectors.toList());
        tags.stream().forEach(u->{
            System.out.println(u.getId());
        });
        String str="";

        if(tags.get(0).getType()==0){
            str="tb_resources";
        }

        if(tags.get(0).getType()==1){
            str="tb_circles";
        }

        //根据二级标签组查询每个标签发过多少个帖子
        List<TagsVo> tagsVos = tagMapper.selectTagsNum(stringList, str);
        return tagsVos;
    }


}
