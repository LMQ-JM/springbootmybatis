package com.example.message.dao;

import com.example.message.entity.ChatLogList;
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
    @Select("select * from tb_chat_log_list where dq_user_id=${userId} or user_id=${userId}")
    List<ChatLogList> queryChatList(@Param("userId") int userId);

    /**+
     * 添加聊天列表
     * @param chatLogList 聊天列表对象
     * @return
     */
    @Insert("insert into tb_chat_log_list(user_id,user_name,avatar,dq_user_id,create_at)values(${chatLogList.userId},#{chatLogList,userName},#{chatLogList,avatar},${chatLogList.dqUserId},#{chatLogList.createAt})")
    int addChatList(@Param("chatLogList") ChatLogList chatLogList);
}
