package com.example.home.controller;

import com.example.circle.entity.Circle;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.Paging;
import com.example.common.utils.ReturnVo;
import com.example.home.dao.HomeMapper;
import com.example.home.entity.Resources;
import com.example.home.service.IHomeService;
import com.example.home.vo.HomeClassificationVo;
import com.example.home.vo.ResourcesVo;
import com.example.home.vo.ha;
import com.example.tags.entity.Tag;
import com.example.user.util.Upload;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
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

    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private Upload upload;

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
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"page不要传0 或者参数错误");
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
    public ResourcesVo selectSingleResourcePost(int id,int userId) throws ParseException {
        return iHomeService.selectSingleResourcePost(id,userId);
    }

    /**
     * 根据二级标签id查询推荐数据 点进帖子详情 触发
     * @param id 二级标签id
     * @return
     */
    @ApiOperation(value = "根据二级标签id查询推荐的数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectRecommendedSecondaryTagId")
    public List<HomeClassificationVo> selectRecommendedSecondaryTagId(int id)  {
        return iHomeService.selectRecommendedSecondaryTagId(id);
    }


    /**
     *
     *  增加资源帖子
     * @return
     */
    @ApiOperation(value = "增加资源帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addResourcesPost")
    public int addResourcesPost(Resources resources) throws Exception {
        if(resources.getCover()==null || resources.getUId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"封面不能为空");
        }
        return iHomeService.addResourcesPost(resources);
    }



    /**
     *  后台
     *  查询所有资源的数据
     * @return
     */
    @ApiOperation(value = "查询所有资源的数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectResourcesAllPosting")
    public ReturnVo selectResourcesAllPosting(Circle circle, Integer page, Integer limit, String startTime, String endTime) throws Exception {
        if(page==null || limit==null){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        ReturnVo returnVo = iHomeService.selectResourcesAllPosting(circle, page, limit, startTime, endTime);

        return returnVo;
    }


    /**
     * 后台
     * 批量删除帖子
     * @return
     */
    @ApiOperation(value = "批量删除帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/resourcesDeletes")
    public Integer resourcesDeletes(@RequestParam("id")  Integer[] id) throws ParseException {
        if(id==null){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        //批量删除
        Integer deletes = iHomeService.resourcesDeletes(id);
        return  deletes;
    }

    /**
     * 发布
     * @return
     */
    @ApiOperation(value = "发布",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/issueResourceOrCircle")
    public void issueResourceOrCircle(Resources resources,String imgUrl,int postType,int whetherCover) throws Exception {
        if(resources.getUId()==0 || resources.getTagsOne()==0 || resources.getTagsTwo()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        iHomeService.issueResourceOrCircle(resources,imgUrl,postType,whetherCover);

    }

    @ApiOperation(value = "文件上传", notes = "文件上传")
    @ResponseBody
    @PostMapping("/uploadFile")
    public List<String> uploadFile(@RequestParam("files") MultipartFile file) {
        List<String> upload = this.upload.upload(file);
        return upload;
    }

    @ApiOperation(value = "删除服务器图片", notes = "删除服务器图片")
    @ResponseBody
    @PostMapping("/deleteFile")
    public void deleteFile(int type,String imgUrl) {
        String substring = imgUrl.substring(imgUrl.lastIndexOf("/"));

        String documentType="";
        //0代表是图片
        if(type==0){
            documentType="img";
        }

        //1代表是视屏
        if(type==1){
            documentType="video";
        }
        File file = new File("e://file/"+documentType+""+substring+"");
        //判断文件是否存在
        if (file.exists()){
            boolean delete = file.delete();
            if(!delete){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"删除服务器文件错误!");
            }
        }else{
            throw new ApplicationException(CodeType.SERVICE_ERROR,"图片不存在!");
        }

    }




    @PostMapping("/addimg")
    public int addimg()  {
        List<String> list=new ArrayList<>();
        List<ha> strings = homeMapper.selectImg();
        for (int i=0;i<strings.size();i++){
            if(strings.get(i).getImg()==null){
                continue;
            }
            String[] split = strings.get(i).getImg().split(",");
            for (int a=0;a<split.length;a++){
                //homeMapper.addimg(strings.get(i).getId(),split[a],System.currentTimeMillis()/1000+"");
            }

        }
        return 0;
    }



}
