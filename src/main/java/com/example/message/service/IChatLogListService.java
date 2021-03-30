package com.example.message.service;

import com.example.message.vo.UsersVo;

/**
 * @author MQ
 * @date 2021/3/19 17:17
 */
public interface IChatLogListService {



    /**
     * 根据用户id查询用户信息
     * @param id 用户id
     * @return
     */
    UsersVo QueryUserInformationBasedUserId(int id);
}
