package com.example.user.dao;

import com.example.user.entity.AdminUser;
import com.example.user.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/1/16 16:41
 */
@Component
public interface UserMapper {

    /**
     * 统计用户数量
     * @param sql
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_user where is_delete=1 ${sql}")
    Integer userCount(@Param("sql")String sql);

    /**
     * 根据条件所有用户信息
     * @param sql 拼接的参数
     * @param paging 分页
     * @return
     */
    @Select("select id,user_name,user_sex,avatar,create_at,m_code  from tb_user where is_delete=1 ${sql} order by create_at desc ${paging}")
    List<User> queryAllUserForSql(@Param("sql")String sql, @Param("paging")String paging);

    /**
     * 查询用户名和密码
     * @param userName
     * @param password
     * @return
     */
    @Select("select *from tb_user_admin where `account`=#{userName} and password=#{password} and is_status=1")
    AdminUser selectUserNamePassword(@Param("userName")String userName, @Param("password")String password);

    /**
     * 增加系统用户
     * @param adminUser
     * @return
     */
    @Insert("insert into tb_user_admin(account,password,role_id,avatar,create_at,update_at)values(#{adminUser.account},#{adminUser.password},${adminUser.roleId},#{adminUser.avatar},#{adminUser.createAt},#{adminUser.updateAt})")
    int addAdminUser(@Param("adminUser") AdminUser adminUser);
}
