package com.example.message.dao;

import com.example.message.entity.ChatLogList;
import com.example.message.entity.ChatRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/19 16:45
 */
@Component
public interface MessageMapper {

    /**
     * 根据用户id查询我的消息聊天列表
     * @param userId 当前登录人id
     * @return
     */
    @Select("select * from tb_chat_log_list where dq_user_id=${userId}")
    List<ChatLogList> queryChatList(@Param("userId") int userId);

    /**+
     * 添加聊天列表
     * @param chatLogList 聊天列表对象
     * @return
     */
    @Insert("insert into tb_chat_log_list(user_id,user_name,avatar,dq_user_id,unique_identification,create_at)values(${chatLogList.userId},#{chatLogList.userName},#{chatLogList.avatar},${chatLogList.dqUserId},#{chatLogList.uniqueIdentification},#{chatLogList.createAt})")
    int addChatList(@Param("chatLogList") ChatLogList chatLogList);

    /**
     * 根据唯一标识查询用户与用户之间最新的一条消息
     * @param uniqueIdentification 唯一标识
     * @return
     */
    @Select("select message tb_chat_record where m_code=#{uniqueIdentification} order by create_at desc limit 1")
    String queryNewestByUniqueIdentification(@Param("uniqueIdentification")long uniqueIdentification);

    /**
     * 根据用户唯一标识查询出用户与用户的聊天记录
     * @param uniqueIdentification 唯一标识
     * @return
     */
    @Select("select * from tb_chat_record where m_code=#{uniqueIdentification}")
    List<ChatRecord> queryChattingRecords(@Param("uniqueIdentification") long uniqueIdentification);

    /**
     * 添加消息
     * @param chatRecord
     * @return
     */
    @Insert("insert into tb_chat_record(fuser_id,juser_id,message,message_type,create_at,read_unread,m_code)"
            + "values(${chatRecord.fUserId},${chatRecord.jUserId},#{chatRecord.message},${chatRecord.messageType},#{chatRecord.createAt},${chatRecord.readUnread},#{chatRecord.mCode})")
    int addMessage(@Param("chatRecord") ChatRecord chatRecord);
}
