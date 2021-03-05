package com.example.circle.controller;

import com.example.circle.entity.Circle;
import com.example.circle.service.ICircleService;
import com.example.circle.vo.CircleClassificationVo;
import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.Paging;
import com.example.common.utils.ReturnVo;
import com.example.user.util.Upload;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/1/19 16:11
 */
@Api(tags = "圈子API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/CircleController")
public class CircleController {

    @Autowired
    private ICircleService iCircleService;

    @Autowired
    private Upload upload;



    /**
     *  后台
     *  查询所有圈子的数据
     * @return
     */
    @ApiOperation(value = "查询所有圈子的数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryAllCircles")
    public ReturnVo queryAllCircles() throws ParseException {
        ReturnVo returnVo = iCircleService.queryAllCircles();
        return returnVo;
    }

    /**
     *
     *  上传文件
     * @return
     */
    @ApiOperation(value = "上传文件",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/uploadFiles")
    public ReturnVo uploadFiles(@Param("files") MultipartFile[] file) throws Exception {
        List<String> uploads = this.upload.uploads(file);
        ReturnVo returnVo=new ReturnVo();
        returnVo.setList(uploads);
        return returnVo;
    }

    /**
     *
     *  增加圈子帖子
     * @return
     */
    @ApiOperation(value = "增加圈子帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addCirclePost")
    public int addCirclePost(Circle circle) throws Exception {
        if(circle.getCover()==null || circle.getUId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"封面不能为空");
        }
        return iCircleService.addCirclePost(circle);
    }


    /**
     *  后台
     *  查询所有圈子的数据
     * @return
     */
    @ApiOperation(value = "查询所有圈子的数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectAllPosting")
    public ReturnVo selectAllPosting(Circle circle,Integer page,Integer limit,String startTime,String endTime) throws Exception {
        if(page==null || limit==null){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        ReturnVo returnVo = iCircleService.selectAllPosting(circle, page, limit, startTime, endTime);

        return returnVo;
    }


    /**
     *
     *  批量删除帖子
     * @return
     */
    @ApiOperation(value = "批量删除帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/postingDeletes")
    public Integer postingDeletes(@RequestParam("id")  Integer[] id) throws ParseException {
        if(id==null){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        //批量删除
        Integer deletes = iCircleService.deletes(id);
        return  deletes;
    }


    /**
     *
     * 根据圈子中的标签id查询帖子
     * @return
     */
    @ApiOperation(value = "根据圈子中的标签id查询帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectPostsBasedTagIdCircle")
    public List<CircleClassificationVo> selectPostsBasedTagIdCircle(int id, Paging paging,int userId) throws ParseException {
        if(id==0 || paging.getPage()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        List<CircleClassificationVo> circles = iCircleService.selectPostsBasedTagIdCircle(id, paging,userId);
        return  circles;
    }

    /**
     *
     * 点赞
     * @return
     */
    @ApiOperation(value = "点赞",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/givePost")
    public int givePost(int id,int userId){
        if(id==0 || userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCircleService.givePost(id, userId);
    }


    /**
     *
     * 查询单个圈子的帖子
     * @return
     */
    @ApiOperation(value = "查询单个圈子的帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/querySingleCircle")
    public CircleClassificationVo querySingleCircle(int id,int userId){
        if(id==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCircleService.querySingleCircle(id, userId);
    }




}
