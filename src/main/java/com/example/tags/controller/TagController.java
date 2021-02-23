package com.example.tags.controller;

import com.example.tags.entity.Tag;
import com.example.tags.service.ITagService;
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
     *  圈子 后台
     *  查询所有圈子的一级标签
     * @return
     */
    @ApiOperation(value = " 查询所有圈子的一级标签",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectAllTag")
    public List<Tag> selectAllTag() {
        List<Tag> tags = iTagService.selectAllTag();
        return tags;
    }

    /**
     * 资源 后台
     * 查询所有资源的一级标签
     * @return
     */
    @ApiOperation(value = "查询所有资源的一级标签",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectResourcesAllTag")
    public List<Tag> selectResourcesAllTag() {
        List<Tag> tags = iTagService.selectResourcesAllTag();
        return tags;
    }

    /**
     *  资源
     *  根据一级标签id查询二级标签
     * @return
     */
    @ApiOperation(value = "根据一级标签id查询二级标签",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectResourcesAllTags")
    public List<Tag> selectResourcesAllTags(int tid) {
        List<Tag> tags = iTagService.selectResourcesAllTags(tid);
        return tags;
    }

    /**
     *  圈子
     *  根据一级标签id查询二级标签
     * @return
     */
    @ApiOperation(value = "根据一级标签id查询二级标签",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectAllTags")
    public List<Tag> selectAllTags(int tid) {
        List<Tag> tags = iTagService.selectAllTags(tid);
        return tags;
    }


}
