package com.example.home.controller;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.Paging;
import com.example.home.entity.RecruitJobSearch;
import com.example.home.service.IRecruitJobSearchService;
import com.example.home.vo.RecruitJobSearchVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/3/30 15:51
 */
@Api(tags = "人才找工作API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/RecruitJobSearchController")
public class RecruitJobSearchController {

    @Autowired
    private IRecruitJobSearchService iRecruitJobSearchService;

    /**
     *
     * 根据条件查询人才里面的找工作信息
     * @return
     */
    @ApiOperation(value = "根据条件查询人才里面的招牌信息",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryJobInformation")
    public List<RecruitJobSearchVo> queryJobInformation(int typeId, Paging paging, int orderBy)  {
        if(paging.getPage()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"page不要传0，page传1 或者参数错误");
        }

        return iRecruitJobSearchService.queryJobInformation(typeId,paging,orderBy);
    }

    /**
     * 查看找工作信息详情
     * @return
     */
    @ApiOperation(value = "查看找工作信息详情",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryJobSearchDetails")
    public RecruitJobSearchVo queryJobSearchDetails (int id)  {
        if(id==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iRecruitJobSearchService.queryJobSearchDetails(id);
    }



    /**
     * 添加找工作数据
     * @return
     */
    @ApiOperation(value = "添加找工作数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addJobHunting")
    public void addJobHunting(RecruitJobSearch recruitJobSearch,Integer[] label) throws ParseException {
         if(label.length==0 || recruitJobSearch.getUserId()==0){
             throw new ApplicationException(CodeType.PARAMETER_ERROR,"标签数组为空,或其他参数错误！");
         }

         iRecruitJobSearchService.addJobHunting(recruitJobSearch,label);
    }

}
