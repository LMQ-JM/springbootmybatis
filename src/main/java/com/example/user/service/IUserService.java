package com.example.user.service;

import com.example.common.utils.ReturnVo;
import com.example.user.entity.AdminUser;
import com.example.user.vo.UserHtVo;

import java.util.Map;

/**
 * @author MQ
 * @date 2021/1/16 16:19
 */
public interface IUserService {


    /**
     * 小程序登陆
     * @param code 前段获得的code码
     * @return
     */
    Map<String,Object> wxLogin(String code);

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
