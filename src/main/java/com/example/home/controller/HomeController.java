package com.example.home.controller;

import com.example.circle.entity.Circle;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.Paging;
import com.example.common.utils.ReturnVo;
import com.example.home.entity.Resources;
import com.example.home.service.IHomeService;
import com.example.home.vo.HomeClassificationVo;
import com.example.posting.vo.PostingVo;
import com.example.tags.entity.Tag;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
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
        List<Circle> circles = iHomeService.selectAllSearch(postingName, paging);

        return circles;
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


    //------------------------资源市场和学习交流接口------------------------
    /**
     *
     *  资源市场和学习交流的接口
     * @return
     */
    @ApiOperation(value = "资源市场和学习交流的接口",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectResourceLearningExchange")
    public List<HomeClassificationVo> selectResourceLearningExchange(int id, Paging paging)  {
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
        return iHomeService.selectPostsByCommunityCategoryId(id,paging);
    }


    //--------------------------------------------资源合作和人才市场------------------------------


    /**
     * 根据一级标签id查询二级标签
     * @param id 一级标签id
     * @return
     */
    @ApiOperation(value = "根据一级标签id查询二级数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectSecondaryLabel")
    public List<Tag> selectSecondaryLabel(int id)  {
        return iHomeService.selectFirstLevelLabelResource();
    }



    /**
     * 根据二级级标签id查询所有帖子
     * @param id 二级级标签id
     * @return
     */
    @ApiOperation(value = "根据二级级标签id查询所有帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectAllPostsSecondaryTagId")
    public List<Resources> selectAllPostsSecondaryTagId(int id,Paging paging){
        return iHomeService.selectAllPostsSecondaryTagId(id,paging);
    }


    /**
     * 进入单元体的接口
     * 根据社区分类id查询帖子
     * @return
     */
    @ApiOperation(value = "根据社区分类id查询帖子 ",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectPostsByCommunityCategoryIds")
    public List<Resources> selectPostsByCommunityCategoryIds(int id, Paging paging)  {
        return iHomeService.selectPostsByCommunityCategoryIds(id,paging);
    }













    /**
     * 根据三级级标签id查询帖子
     * @param id 一级标签id
     * @return
     */
    @ApiOperation(value = "根据二级级标签id查询帖子 如果还有第三级标签 先查询出第三级标签",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectPostingByThreeLabelId")
    public List<Resources> selectPostingByThreeLabelId(int id, Paging paging)  {
        if(paging.getPage()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"page不要传0，传1");
        }
        List<Resources> resources = iHomeService.selectPostingByThreeLabelId(id, paging);
        return resources;
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
        Resources resources = iHomeService.selectSingleResourcePost(id);
        return resources;
    }



}
