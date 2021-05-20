package com.example.inform.controller;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.Paging;
import com.example.inform.service.IInformService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author MQ
 * @date 2021/3/22 10:36
 */
@Api(tags = "通知API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/InformController")
public class InformController {

    @Autowired
    private IInformService iInformService;

    /**
     * 查询评论，获赞通知
     * @return
     */
    @ApiOperation(value = "查询评论，获赞通知",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryCommentsNotice")
    public Map<String,Object> queryCommentsNotice(int userId, int type, Paging paging)  {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iInformService.queryCommentsNotice(userId,type,paging);
    }
}
