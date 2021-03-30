package com.example.message.controller;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.message.service.IChatLogListService;
import com.example.message.vo.UsersVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author MQ
 * @date 2021/3/19 17:15
 */
@Api(tags = "消息API")
@CrossOrigin(origins = "*", maxAge = 100000)
@RestController
@Slf4j
@RequestMapping("/ChatLogListController")
public class ChatLogListController {

    @Autowired
    private IChatLogListService iChatLogListService;



    /**
     * 根据用户id查询用户信息
     * @return
     */
    @ApiOperation(value = "根据用户id查询用户信息",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/QueryUserInformationBasedUserId")
    public UsersVo QueryUserInformationBasedUserId(int id) {
        if(id==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"参数错误");
        }
       return iChatLogListService.QueryUserInformationBasedUserId(id);
    }

}
