package com.example.message.service.impl;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.message.dao.MessageMapper;
import com.example.message.entity.ChatLogList;
import com.example.message.service.IChatLogListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/19 17:17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ChatLogListServiceImpl implements IChatLogListService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public int addChatList(ChatLogList chatLogList) {
        chatLogList.setCreateAt(System.currentTimeMillis()/1000+"");
        int i = messageMapper.addChatList(chatLogList);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加聊天列表失败！");
        }
        return i;
    }

    @Override
    public List<ChatLogList> queryChatList(int userId) {

        List<ChatLogList> chatLogLists = messageMapper.queryChatList(userId);
        chatLogLists.stream().forEach(u->{

        });

        return null;
    }


}
