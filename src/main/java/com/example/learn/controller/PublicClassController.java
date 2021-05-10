package com.example.learn.controller;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.learn.service.IPublicClassService;
import com.example.learn.vo.PublicClassVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * @author JC
 * @date 2021/5/4 17:04
 */
@Api(tags = "公开课API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/PublicClassController")
public class PublicClassController {

    @Autowired
    private IPublicClassService iPublicClassService;

    /**
     * 根据id查询公开课详情
     * @param id
     * @param userId
     * @return
     * @throws ParseException
     */
    @ApiOperation(value = "根据id查询公开课详情",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryPublicClassById")
    public PublicClassVo queryPublicClassById(int id,int userId) throws Exception{
        if(id==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iPublicClassService.queryPublicClassById(id,userId);
    }

    /**
     * 公开课收藏
     * @param id 公开课id
     * @param userId 收藏人id
     * @return
     */
    @ApiOperation(value = "收藏",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/giveCollect")
    public int giveCollect(int id, int userId){
        if(id==0 || userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iPublicClassService.giveCollect(id,userId);
    }
}
