package com.example.message.service.impl;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.IdGenerator;
import com.example.message.dao.MessageMapper;
import com.example.message.entity.ChatLogList;
import com.example.message.entity.ChatRecord;
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

    @Autowired
    private IdGenerator idGenerator;

    @Override
    public long addChatList(ChatLogList chatLogList) {

        //查询是否存在数据库
        List<ChatLogList> chatLogLists = messageMapper.queryChatList(chatLogList.getDqUserId());

        //得到用户与用户之间的唯一标识
        long numberId = idGenerator.getNumberId();

        //不存在则添加
        if(chatLogLists.size()==0 || chatLogLists==null){

            chatLogList.setUniqueIdentification(numberId);
            chatLogList.setCreateAt(System.currentTimeMillis()/1000+"");

            //添加用户聊天列表
            int i = messageMapper.addChatList(chatLogList);
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"添加聊天列表失败！");
            }
        }
        return numberId;

    }

    @Override
    public List<ChatLogList> queryChatList(int userId) {

        List<ChatLogList> chatLogLists = messageMapper.queryChatList(userId);
        chatLogLists.stream().forEach(u->{
            //根据唯一标识去查询用户与用户最新的一条聊天记录
            String s = messageMapper.queryNewestByUniqueIdentification(u.getUniqueIdentification());
            u.setNewestChatLog(s);
        });

        return chatLogLists;
    }

    @Override
    public List<ChatRecord> queryChattingRecords(long uniqueIdentification) {
        List<ChatRecord> chatRecords = messageMapper.queryChattingRecords(uniqueIdentification);

        return chatRecords;
    }


}
