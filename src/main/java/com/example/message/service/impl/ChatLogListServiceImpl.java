package com.example.message.service.impl;

import com.example.message.service.IChatLogListService;
import com.example.message.vo.UsersVo;
import com.example.user.dao.UserMapper;
import com.example.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author MQ
 * @date 2021/3/19 17:17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ChatLogListServiceImpl implements IChatLogListService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public UsersVo QueryUserInformationBasedUserId(int id) {
        User user = userMapper.selectUserById(id);

        UsersVo usersVo=new UsersVo();
        usersVo.setAvatar(user.getAvatar());
        usersVo.setId(user.getId());
        usersVo.setUserName(user.getUserName());

        return usersVo;
    }




}
