package com.example.tags.controller;

import com.example.tags.entity.Tag;
import com.example.tags.service.ITagService;
import com.example.tags.vo.TagsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MQ
 * @date 2021/1/21 16:07
 */
@Api(tags = "标签API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/TagController")
public class TagController {


    @Autowired
    private ITagService iTagService;


    /**
     * 根据参数类型查询不同类型的标签
     * @return
     */
    @ApiOperation(value = "根据参数类型查询不同类型的标签",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectResourcesTag")
    public List<Tag> selectResourcesTag(int type) {
        List<Tag> tags = iTagService.selectResourcesAllTag(type);
        return tags;
    }


    /**
     *  根据一级标签id查询二级标签
     * @return
     */
    @ApiOperation(value = "根据一级标签id查询二级标签",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectResourcesAllTags")
    public List<TagsVo> selectResourcesAllTags(int tid) {
        List<TagsVo> tags = iTagService.selectResourcesAllTags(tid);

        return tags;
    }

}
