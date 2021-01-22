package com.example.user.service;

import com.example.common.utils.ReturnVo;
import com.example.user.entity.AdminUser;
import com.example.user.vo.UserHtVo;

/**
 * @author MQ
 * @date 2021/1/16 16:19
 */
public interface IUserService {

    /**
     * 后台查询所有后台
     * @param userHtVo
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    ReturnVo queryAllUserForSql(UserHtVo userHtVo, Integer pageNum, Integer pageSize) throws Exception;

    /**
     * 登录
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    AdminUser selectUserNamePassword(String userName, String password);

    /**
     * 增加系统用户
     * @param adminUser
     * @return
     */
    int addAdminUser(AdminUser adminUser);
}
