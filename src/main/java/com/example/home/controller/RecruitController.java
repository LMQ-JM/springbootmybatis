package com.example.home.controller;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.Paging;
import com.example.home.service.IRecruitService;
import com.example.home.vo.RecruitVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MQ
 * @create 2021/2/20
 **/
@Api(tags = "人才API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/RecruitController")
public class RecruitController {

    @Autowired
    private IRecruitService iRecruitService;

    /**
     *
     * 根据条件查询人才里面的招牌信息
     * @return
     */
    @ApiOperation(value = "根据条件查询人才里面的招牌信息",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectSignboardInformation")
    public List<RecruitVo> selectSignboardInformation(int typeId, Paging paging,int userId,int orderBy)  {
        if(paging.getPage()==0 || typeId>5){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"page不要传0，page传1 或者参数错误");
        }

        return iRecruitService.selectSignboardInformation(typeId,paging,userId,orderBy);
    }

    /**
     *
     * 增加浏览量
     * @return
     */
    @ApiOperation(value = "增加浏览量",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/increasePageViews")
    public int increasePageViews(int id)  {
        if(id==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iRecruitService.increasePageViews(id);
    }

    /**
     *
     * 查看招聘信息详情
     * @return
     */
    @ApiOperation(value = "查看招聘信息详情",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectViewDetails")
    public RecruitVo selectViewDetails (int id)  {
        if(id==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iRecruitService.selectViewDetails(id);
    }

    /**
     *
     * 根据用户id查询发布的招聘信息
     * @return
     */
    @ApiOperation(value = "根据用户id查询发布的招聘信息",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectPostingBasedUserID")
    public List<RecruitVo> selectPostingBasedUserID (int userId, Paging paging)  {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iRecruitService.selectPostingBasedUserID(userId,paging);
    }


}
