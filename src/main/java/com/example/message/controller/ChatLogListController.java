package com.example.message.controller;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.message.entity.ChatLogList;
import com.example.message.service.IChatLogListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     *
     *  添加聊天列表
     * @return
     */
    @ApiOperation(value = "添加聊天列表",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addChatList")
    public int addChatList(ChatLogList chatLogList) {
        if(chatLogList.getUserId()==0 || chatLogList.getDqUserId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"参数错误");
        }

        return iChatLogListService.addChatList(chatLogList);
    }

    /**
     *
     * 查询聊天列表
     * @return
     */
    @ApiOperation(value = "查询聊天列表",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryChatList")
    public List<ChatLogList> queryChatList(int userId) {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"参数错误");
        }

        return iChatLogListService.queryChatList(userId);
    }
}
