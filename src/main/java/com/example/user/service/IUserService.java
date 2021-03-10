package com.example.user.service;

import com.example.common.utils.ReturnVo;
import com.example.user.entity.AdminUser;
import com.example.user.entity.LoginTag;
import com.example.user.entity.User;
import com.example.user.entity.UserTag;
import com.example.user.vo.UserHtVo;

import java.util.List;

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
    User wxLogin(String code, String userName, String avatar, String address,String sex);

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
    User selectUserNamePassword(String userName, String password);

    /**
     * 增加系统用户
     * @param adminUser
     * @return
     */
    int addAdminUser(AdminUser adminUser);

    /**
     * 查询所以标签
     * @return
     */
    List<LoginTag> selectAllUserLabel();

    /**
     * 增加用户选中的标签关系
     * @param userTag 标签对象
     * @return
     */
    int addUserAndLabel(UserTag userTag);

    /**
     * 修改用户选中的标签关系
     * @param userTag 标签对象
     * @return
     */
    int updateUserAndLabel(UserTag userTag);



    /**
     * 根据用户id查询是否有登录进来的时候选中过标签
     * @param userId
     * @return
     */
    int selectWhetherHaveLabel(int userId);

    /**
     * 根据id查询
     * @param userId 用户id
     * @return
     */
    User selectUserById(int userId);
}
