package com.example.posting.controller;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.ReturnVo;
import com.example.posting.service.IPostingService;
import com.example.posting.vo.PostingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * @author MQ
 * @date 2021/1/15 10:06
 */
@Api(tags = "帖子API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/PostingController")
public class PostingController {


    @Autowired
    private IPostingService iPostingService;

    /**
     *
     *  根据条件查询帖子
     * @return
     */
    @ApiOperation(value = "根据条件查询帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectAllPosting")
    public ReturnVo selectAllPosting(PostingVo postingVo) throws ParseException {
        if(postingVo.getPage()==null || postingVo.getLimit()==null){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        ReturnVo returnVo = iPostingService.selectAllPosting(postingVo);
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
        Integer deletes = iPostingService.deletes(id);
        return  deletes;
    }

}
