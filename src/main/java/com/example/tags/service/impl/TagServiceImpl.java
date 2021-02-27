package com.example.tags.service.impl;

import com.example.tags.dao.TagMapper;
import com.example.tags.entity.Tag;
import com.example.tags.service.ITagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Tag> selectAllTag() {
        List<Tag> tags = tagMapper.selectAllTag();
        return tags;
    }

    @Override
    public List<Tag> selectAllTags(int tid) {
        List<Tag> tags = tagMapper.selectAllTags(tid);
        return tags;
    }

    @Override
    public List<Tag> selectResourcesAllTag() {
        List<Tag> tags = tagMapper.selectResourcesAllTag();
        for (int i=0;i<tags.size();i++){
            if(tags.get(i).getId()==14){
                tags.remove(i);
            }
        }
        return tags;
    }

    @Override
    public List<Tag> selectResourcesAllTags(int tid) {
        List<Tag> tags = tagMapper.selectResourcesAllTags(tid);
        return tags;
    }


}
