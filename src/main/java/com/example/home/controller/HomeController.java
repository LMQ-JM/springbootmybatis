package com.example.home.controller;

import com.example.circle.entity.Circle;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.Paging;
import com.example.home.entity.Resources;
import com.example.home.service.IHomeService;
import com.example.tags.entity.Tag;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author Administrator
 */
@Api(tags = "首页API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/HomeController")
public class HomeController {

    @Autowired
    private IHomeService iHomeService;

    /**
     *
     *  搜索数据接口
     * @return
     */
    @ApiOperation(value = "搜索数据接口",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectAllSearch")
    public List<Circle> selectAllSearch(String postingName, Paging paging)  {
        if(paging.getPage()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"page不要传0，page传1");
        }

        return iHomeService.selectAllSearch(postingName, paging);
    }


    /**
     *
     *  查询资源的第一级标签
     * @return
     */
    @ApiOperation(value = "查询资源的第一级标签",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectFirstLevelLabelResource")
    public List<Tag> selectFirstLevelLabelResource()  {
        return iHomeService.selectFirstLevelLabelResource();
    }



    /**
     *
     * 根据一级标签id查询所有下面的数据
     * @return
     */
    @ApiOperation(value = "根据一级标签id查询所有下面的数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectResourceLearningExchange")
    public Object selectResourceLearningExchange(int id, Paging paging)  {
        if(paging.getPage()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"page不要传0");
        }
        return iHomeService.selectResourceLearningExchange(id,paging);
    }

    /**
     * 进入单元体的接口
     * 根据社区分类id查询帖子
     * @return
     */
    @ApiOperation(value = "根据社区分类id查询帖子 ",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectPostsByCommunityCategoryId")
    public List<Resources> selectPostsByCommunityCategoryId(int id, Paging paging)  {
        if(paging.getPage()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"page不要传0");
        }
        return iHomeService.selectPostsByCommunityCategoryId(id,paging);
    }

    /**
     * 查询单个资源帖子
     * @param id 资源帖子id
     * @return
     */
    @ApiOperation(value = "查询单个资源帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectSingleResourcePost")
    public Resources selectSingleResourcePost(int id)  {
        return iHomeService.selectSingleResourcePost(id);
    }



}
