package com.example.user.dao;

import com.example.user.entity.AdminUser;
import com.example.user.entity.LoginTag;
import com.example.user.entity.User;
import com.example.user.entity.UserTag;
import org.apache.ibatis.annotations.*;
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

    /**
     * 根据openid查询用户是否存在
     * @param openId
     * @return
     */
    @Select("select * from tb_user where open_id=#{openId}")
    User selectUserByOpenId(@Param("openId") String openId);

    /**
     * 根据id查询用户
     * @param Id
     * @return
     */
    @Select("select * from tb_user where id=#{Id}")
    User selectUserById(@Param("Id") int Id);

    /**
     * 根据用户名查询用户是否存在
     * @param userName
     * @return
     */
    @Select("select * from tb_user_admin where user_name=#{userName}")
    AdminUser selectUserByUserName(@Param("userName") String userName);

    /**
     * 增加用户信息
     * @param user 用户对象
     * @return
     */
    @Insert("insert into tb_user(user_name,open_id,avatar,create_at)values(#{user.userName},#{user.openId},#{user.avatar},#{user.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="user.id", keyColumn="id")
    int addUser(@Param("user") User user);

    /**
     * 查询用户表中的最大id
     * @return
     */
    @Select("select max(id) from tb_user")
    int selectMaxId();

    /**
     * 查询所有标签
     * @return
     */
    @Select("select * from tb_login_tag ")
    List<LoginTag> selectAllUserLabel();



    /**
     * 增加用户和标签的关系
     * @param userTag
     * @return
     */
    @Insert("insert into tb_user_tag(u_id,tab,create_at)values(${userTag.uId},#{userTag.tab},#{userTag.createAt})")
    int addUserAndLabel(@Param("userTag") UserTag userTag);

    /**
     * 删除用户选中的标签
     * @param uId
     * @return
     */
    @Update("delete  from tb_user_tag where u_id=${uId}")
    int deleteUserAndLabel(@Param("uId") int uId);

    /**
     * 根据用户id查询是否有登录进来的时候选中过标签
     * @param uId
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_user_tag where u_id=${uId}")
    int selectWhetherHaveLabel(@Param("uId") int uId);


    /**
     * 随机查询
     * @return
     */
    @Select("select * from tb_user where is_delete=1 limit 5")
    List<User> selectRandom();
}

