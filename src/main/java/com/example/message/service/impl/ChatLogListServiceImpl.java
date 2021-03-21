package com.example.message.service.impl;

import com.example.common.constanct.CodeType;
import com.example.common.exception.ApplicationException;
import com.example.common.utils.IdGenerator;
import com.example.message.dao.MessageMapper;
import com.example.message.entity.ChatLogList;
import com.example.message.service.IChatLogListService;
import com.example.message.vo.ChatRecordUserVo;
import com.example.message.vo.ChatRecordVo;
import com.example.message.vo.UsersVo;
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
    public String addChatList(ChatLogList chatLogList) {

        //查询是否存在数据库
        List<ChatLogList> chatLogLists = messageMapper.queryChatLists(chatLogList.getDqUserId(),chatLogList.getUserId());

        //得到用户与用户之间的唯一标识
        long numberId = idGenerator.getNumberId();

        //不存在则添加
        if(chatLogLists.size()==0 || chatLogLists==null){

            chatLogList.setUniqueIdentification(String.valueOf(numberId));
            chatLogList.setCreateAt(System.currentTimeMillis()/1000+"");

            //添加用户聊天列表
            int i = messageMapper.addChatList(chatLogList);
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"添加聊天列表失败！");
            }
        }

        return String.valueOf(numberId);

    }

    @Override
    public List<ChatLogList> queryChatList(int userId) {

        List<ChatLogList> chatLogLists = messageMapper.queryChatList(userId);
        for (int i=0;i<chatLogLists.size();i++){
            //根据唯一标识去查询用户与用户最新的一条聊天记录
            ChatRecordVo chatRecordVo = messageMapper.queryNewestByUniqueIdentification(Long.valueOf(chatLogLists.get(i).getUniqueIdentification()));


            if(chatRecordVo!=null) {
                chatLogLists.get(i).setNewestChatLog(chatRecordVo.getMessage());

                if (chatRecordVo.getMessageType() == 1) {
                    chatLogLists.get(i).setNewestChatLog("[图片]");
                }

                if (chatRecordVo.getMessageType() == 2) {
                    chatLogLists.get(i).setNewestChatLog("[视屏]");
                }
            }

        }


        return chatLogLists;
    }

    @Override
    public List<ChatRecordUserVo> queryChattingRecords(long uniqueIdentification) {

        //查询聊天记录
        List<ChatRecordUserVo> chatRecords = messageMapper.queryChattingRecords(uniqueIdentification);

        if(chatRecords.size()!=0){
            chatRecords.stream().forEach(u->{
                //得到双方用户信息
                UsersVo usersVo = messageMapper.queryUsersById(u.getFUserId());

                u.setUsersVo(usersVo);
            });

        }

        return chatRecords;
    }


}
